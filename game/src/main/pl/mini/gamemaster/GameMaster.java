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
import pl.mini.position.Position;
import pl.mini.utils.ConsoleColors;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.*;

public class GameMaster {
    @Getter @Setter private int portNumber;
    @Getter @Setter private InetAddress ipAddress;
    @Getter @Setter private List<UUID> teamRedGuids;
    @Getter @Setter private List<UUID> teamBlueGuids;
    @Getter @Setter private GameMasterBoard board;
    @Getter @Setter private GameMasterStatus status;
    @Getter @Setter private GameMasterConfiguration configuration;


    public GameMaster()
    {

    }

    public void StartGame()
    {

    }

    public void listen()
    {

    }

    public GameMasterConfiguration loadConfigurationFromJson(String path)
    {
        GameMasterConfiguration finalConf = new GameMasterConfiguration();
        JSONParser jsonParser = new JSONParser();
        Point tmp;
        List<Point> points = new ArrayList<Point>();

        try (FileReader reader = new FileReader(path))
        {
            JSONObject conf = (JSONObject) jsonParser.parse(reader);
            JSONObject arg = (JSONObject)conf.get("GameMasterConfiguration");
            finalConf.shamProbability = (double)arg.get("shamProbability");
            finalConf.maxTeamSize = ((Long)arg.get("maxTeamSize")).intValue();
            finalConf.maxPieces = ((Long)arg.get("maxPieces")).intValue();

            JSONArray pointList = (JSONArray) arg.get("predefinedGoalPositions");
            for(Object obj : pointList){
                if(obj instanceof JSONObject){
                    tmp = new Point();
                    tmp.x = (int)Math.round((Double)(((JSONObject) obj).get("x")));
                    tmp.y = (int)Math.round((Double)(((JSONObject) obj).get("y")));
                    points.add(tmp);
                }
            }
            finalConf.predefinedGoalPositions = new Point[points.size()];
            points.toArray(finalConf.predefinedGoalPositions);

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

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

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

    public void putNewPiece()
    {
        Random r = new Random();
        Position placed = this.board.generatePiece(r.nextDouble());
        this.board.getPiecesPosition().add(placed);
        this.board.getCellsGrid()[placed.getX()][placed.getY()].cellState = CellState.Piece;
    }

    public void printBoard()
    {
        int col = this.board.getBoardWidth();
        int row = this.board.getBoardHeight();
        int task = this.board.getTaskAreaHeight();
        int goal = this.board.getGoalAreaHeight();
        Set<Position> positions = this.board.getPiecesPosition();
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
                if(cll.playerGuids != "" && cll.playerGuids != null)
                {
                    for(int k =0; k < this.teamBlueGuids.size(); k++) {
                        if (cll.playerGuids.equals(this.teamBlueGuids.get(k).toString()))
                            fld.append("| " + ConsoleColors.BLUE + "B P " + ConsoleColors.RESET);
                    }
                    for (int k = 0; k < this.teamRedGuids.size(); k++) {
                        if (cll.playerGuids.equals(this.teamRedGuids.get(k).toString()))
                            fld.append("| " + ConsoleColors.RED + "R P " + ConsoleColors.RESET);
                    }
                } else if (cState == CellState.Piece || cState == CellState.Sham)
                    fld.append("|  " + ConsoleColors.GREEN + "P  " + ConsoleColors.RESET);
                else if (cState == CellState.Valid)
                    fld.append("|  " + ConsoleColors.YELLOW + "V  " + ConsoleColors.RESET);
                else if (cState == CellState.Goal)
                    fld.append("|  G  ");
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

    public void messageHandler(String message)
    {

    }
}