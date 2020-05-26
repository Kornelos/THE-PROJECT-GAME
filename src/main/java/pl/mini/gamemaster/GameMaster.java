package pl.mini.gamemaster;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.mini.board.GameMasterBoard;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.utils.ConsoleColors;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameMaster {
    @Getter
    @Setter
    private int portNumber;
    @Getter
    @Setter
    private InetAddress ipAddress;
    @Getter
    @Setter
    private List<UUID> teamRedGuids = new ArrayList<>();
    @Getter
    @Setter
    private List<UUID> teamBlueGuids = new ArrayList<>();
    @Getter
    @Setter
    private GameMasterBoard board;
    @Getter
    @Setter
    private GameMasterStatus status;
    @Getter
    @Setter
    private GameMasterConfiguration configuration;
    private GameMasterClient gmClient;

    // class used for Flask UI
    final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public GameMaster() {

    }

    public GameMasterConfiguration loadConfigurationFromJson(String path)
    {
        GameMasterConfiguration finalConf = new GameMasterConfiguration();
        JSONParser jsonParser = new JSONParser();
        Position tmp;
        List<Position> positions = new ArrayList<Position>();

        try (FileReader reader = new FileReader(new File(path))) {
            JSONObject conf = (JSONObject) jsonParser.parse(reader);
            JSONObject arg = (JSONObject) conf.get("GameMasterConfiguration");
            finalConf.shamProbability = (double) arg.get("shamProbability");
            finalConf.maxTeamSize = ((Long) arg.get("maxTeamSize")).intValue();
            finalConf.maxPieces = ((Long) arg.get("maxPieces")).intValue();

            JSONArray pointList = (JSONArray) arg.get("predefinedGoalPositions");
            for (Object obj : pointList) {
                if (obj instanceof JSONObject) {
                    int x = ((Long)((JSONObject) obj).get("x")).intValue();
                    int y = ((Long)((JSONObject) obj).get("y")).intValue();
                    tmp = new Position(x,y);
                    positions.add(tmp);
                }
            }
            finalConf.predefinedGoalPositions = new Position[positions.size()];
            positions.toArray(finalConf.predefinedGoalPositions);

            finalConf.boardWidth = ((Long)arg.get("boardWidth")).intValue();
            finalConf.boardTaskHeight = ((Long) arg.get("boardTaskHeight")).intValue();
            finalConf.boardGoalHeight = ((Long) arg.get("boardGoalHeight")).intValue();
            finalConf.delayDestroyPiece = ((Long) arg.get("delayDestroyPiece")).intValue();
            finalConf.delayNextPiecePlace = ((Long) arg.get("delayNextPiecePlace")).intValue();
            finalConf.delayMove = ((Long) arg.get("delayMove")).intValue();
            finalConf.delayDiscover = ((Long) arg.get("delayDiscover")).intValue();
            finalConf.delayTest = ((Long) arg.get("delayTest")).intValue();
            finalConf.delayPick = ((Long) arg.get("delayPick")).intValue();
            finalConf.delayPlace = ((Long) arg.get("delayPlace")).intValue();

            // initialize board
            initBoard(finalConf.boardWidth, finalConf.boardGoalHeight, finalConf.boardTaskHeight);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        this.setConfiguration(finalConf);
        return finalConf;
    }

    public void saveConfigurationToJson(String path)
    {
        JSONObject conf = new JSONObject();
        JSONArray goalPos = new JSONArray();

        conf.put("shamProbability", configuration.shamProbability);
        conf.put("maxTeamSize", configuration.maxTeamSize);
        conf.put("maxPieces", configuration.maxPieces);

        for (int i = 0; i<configuration.predefinedGoalPositions.length; i++) {
            JSONObject point = new JSONObject();
            point.put("x", configuration.predefinedGoalPositions[i].getX());
            point.put("y", configuration.predefinedGoalPositions[i].getY());
            goalPos.add(point);
        }

        conf.put("predefinedGoalPositions", goalPos);
        conf.put("boardWidth", configuration.boardWidth);
        conf.put("boardTaskHeight", configuration.boardTaskHeight);
        conf.put("boardGoalHeight", configuration.boardGoalHeight);
        conf.put("delayDestroyPiece", configuration.delayDestroyPiece);
        conf.put("delayNextPiecePlace", configuration.delayNextPiecePlace);
        conf.put("delayMove", configuration.delayMove);
        conf.put("delayDiscover", configuration.delayDiscover);
        conf.put("delayTest", configuration.delayTest);
        conf.put("delayPick", configuration.delayPick);
        conf.put("delayPlace", configuration.delayPlace);

        JSONObject gmConf = new JSONObject();
        gmConf.put("GameMasterConfiguration", conf);

        try (FileWriter file = new FileWriter(path)) {

            file.write(gmConf.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRandomGoals(int number)
    {
        for(int i = 0; i < number; i++)
        {
            Random randomX = new Random();
            Random randomY = new Random();
            int x = randomX.nextInt(board.getBoardWidth());
            int y = randomY.nextInt(board.getGoalAreaHeight());
            board.setGoal(new Position(x,y));
            randomX = new Random();
            randomY = new Random();
            x = randomX.nextInt(board.getBoardWidth());
            y = board.getBoardHeight() - board.getGoalAreaHeight() + randomY.nextInt(board.getGoalAreaHeight());
            board.setGoal(new Position(x,y));
        }
    }

    public void setPredefinedGoals(Position[] positions)
    {
        for (var elem : positions)
        {
            board.setGoal(elem);
        }
    }

    public void putNewPiece()
    {
        Random r = new Random();
        Position placed = this.board.generatePiece();
        this.board.getPiecesPosition().add(placed);
        this.board.getCellsGrid()[placed.getX()][placed.getY()].cellState = CellState.Piece;
        for(int i = 0; i < 2 * board.getGoalAreaHeight() + board.getTaskAreaHeight(); i++)
        {
            for(int j = 0; j < board.getBoardWidth(); j++)
            {
                Position pos = new Position(j, i);
                this.board.getCellsGrid()[j][i].distance = this.board.manhattanDistanceToClosestPiece(pos);
            }
        }
    }

    public void putNewPiece(Position placed)
    {
        this.board.getPiecesPosition().add(placed);
        this.board.getCellsGrid()[placed.getX()][placed.getY()].cellState = CellState.Piece;
        for (int i = 0; i < 2*board.getGoalAreaHeight() + board.getTaskAreaHeight(); i++) {
            for (int j = 0; j < board.getBoardWidth(); j++) {
                Position pos = new Position(j, i);
                this.board.getCellsGrid()[j][i].distance = this.board.manhattanDistanceToClosestPiece(pos);
            }
        }
    }

    private void initBoard(int width, int height, int taskAreaHeight) {
        board = new GameMasterBoard(width, height, taskAreaHeight);
    }

    public void printBoard() {
        int col = this.board.getBoardWidth();
        int row = this.board.getBoardHeight();
        int task = this.board.getTaskAreaHeight();
        int goal = this.board.getGoalAreaHeight();
        StringBuilder fld;
        Cell cll;
        CellState cState;

        for (int i = 0; i < row; i++)
        {
            if (i < goal || i > goal + task - 1)
                fld = new StringBuilder("G");
            else
                fld = new StringBuilder("T");
            System.out.println(" " + "######".repeat(col) + "#");
            System.out.println(" " + "|     ".repeat(col) + "|");
            for (int j = 0; j < col; j++)
            {
                cll = this.board.getCellsGrid()[j][i];
                cState = cll.cellState;
                if (!cll.playerGuid.equals("")) {
                    for (UUID teamBlueGuid : this.teamBlueGuids) {
                        if (cll.playerGuid.equals(teamBlueGuid.toString()))
                            fld.append("| " + ConsoleColors.BLUE + "B P " + ConsoleColors.RESET);
                    }
                    for (UUID teamRedGuid : this.teamRedGuids) {
                        if (cll.playerGuid.equals(teamRedGuid.toString()))
                            fld.append("| " + ConsoleColors.RED + "R P " + ConsoleColors.RESET);
                    }
                } else if (cState == CellState.Piece || cState == CellState.Sham)
                    fld.append("|  " + ConsoleColors.GREEN + "P  " + ConsoleColors.RESET);
                else if (cState == CellState.Valid)
                    fld.append("|  " + ConsoleColors.YELLOW + "V  " + ConsoleColors.RESET);
                else if (cState == CellState.Goal)
                    fld.append("|  " + ConsoleColors.PURPLE_BRIGHT + "G  " + ConsoleColors.RESET);
                else if (cState == CellState.Unknown)
                    fld.append("|  U  ");
                else if (cState == CellState.Empty)
                    fld.append("|     ");

            }
            System.out.println(fld + "|");
            System.out.println(" " + "|     ".repeat(col) + "|");
        }
        System.out.println(" " + "######".repeat(col) + "#");
    }

    public void sendBoardState() {
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < board.getBoardHeight(); j++) {
            for (int i = 0; i < board.getBoardWidth(); i++) {
                String boardContent = null;
                if (board.getCellsGrid()[i][j].playerGuid.equals("")) {
                    switch (board.getCellsGrid()[i][j].cellState) {
                        case Empty:
                            boardContent = "Empty";
                            break;
                        case Piece:
                        case Sham:
                            boardContent = "Piece";
                            break;
                        case Goal:
                            boardContent = "Goal";
                            break;
                        case Unknown:
                            boardContent = "Unknown";
                            break;
                        case Valid:
                            boardContent = "Valid";
                            break;
                    }
                } else {
                    for (UUID teamBlueGuid : this.teamBlueGuids) {
                        if (board.getCellsGrid()[i][j].playerGuid.equals(teamBlueGuid.toString())) {
                            boardContent = "BluePlayer";
                            break;
                        }
                    }
                    if (boardContent == null)
                        for (UUID teamRedGuid : this.teamRedGuids) {
                            if (board.getCellsGrid()[i][j].playerGuid.equals(teamRedGuid.toString())) {
                                boardContent = "RedPlayer";
                                break;
                            }
                        }
                }
                jsonArray.add(boardContent);
            }
        }
        // create new JSON
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boardWidth", board.getBoardWidth());
        jsonObject.put("taskAreaHeight", board.getTaskAreaHeight());
        jsonObject.put("goalAreaHeight", board.getGoalAreaHeight());
        jsonObject.put("board", jsonArray);

        //post to html
        try {
            String FLASK_URL = "http://127.0.0.1:5000/post_board";
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .uri(URI.create(FLASK_URL))
                    .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
            System.out.println("Flask UI error: " + e.getClass());
        }
    }

    public TeamColor assignPlayerToTeam(PlayerDTO playerDTO) {
        int teamSize = configuration.maxTeamSize;
        if (teamRedGuids.size() < teamSize) {
            teamRedGuids.add(playerDTO.getPlayerUuid());
            return TeamColor.Red;
        } else if (teamBlueGuids.size() < teamSize) {
            teamBlueGuids.add(playerDTO.getPlayerUuid());
            return TeamColor.Blue;
        }
        // if both teams full
        return null;
    }


}