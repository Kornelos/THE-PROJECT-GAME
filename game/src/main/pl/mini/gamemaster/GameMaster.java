package pl.mini.gamemaster;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.mini.board.GameMasterBoard;
import pl.mini.cell.CellState;
import pl.mini.position.Position;

import java.awt.*;
import java.io.FileNotFoundException;
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

            finalConf.shamProbability = (double)conf.get("shamProbability");
            finalConf.maxTeamSize = ((Long)conf.get("maxTeamSize")).intValue();
            finalConf.maxPieces = ((Long)conf.get("maxPieces")).intValue();

            JSONArray pointList = (JSONArray) conf.get("predefinedGoalPositions");
            for(Object obj : pointList){
                if(obj instanceof JSONObject){
                    tmp = new Point();
                    tmp.x = ((Long)((JSONObject) obj).get("x")).intValue();
                    tmp.y = ((Long)((JSONObject) obj).get("y")).intValue();
                    points.add(tmp);
                }
            }
            finalConf.predefinedGoalPositions = new Point[points.size()];
            points.toArray(finalConf.predefinedGoalPositions);

            finalConf.boardWidth = ((Long)conf.get("boardWidth")).intValue();
            finalConf.boardTaskHeight = ((Long)conf.get("boardTaskHeight")).intValue();
            finalConf.boardGoalHeight = ((Long)conf.get("boardGoalHeight")).intValue();
            finalConf.delayDestroyPiece = ((Long)conf.get("delayDestroyPiece")).intValue();
            finalConf.delayNextPiecePlace = ((Long)conf.get("delayNextPiecePlace")).intValue();
            finalConf.delayMove = ((Long)conf.get("delayMove")).intValue();
            finalConf.delayDiscover = ((Long)conf.get("delayDiscover")).intValue();
            finalConf.delayTest = ((Long)conf.get("delayTest")).intValue();
            finalConf.delayPick = ((Long)conf.get("delayPick")).intValue();
            finalConf.delayPlace = ((Long)conf.get("delayPlace")).intValue();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalConf;
    }

    public void saveConfigurationToJson(String path)
    {
        JSONObject conf = new JSONObject();
        JSONArray goalPos = new JSONArray();
        JSONObject point = new JSONObject();

        conf.put("shamProbability", configuration.shamProbability);
        conf.put("maxTeamSize", configuration.maxTeamSize);
        conf.put("maxPieces", configuration.maxPieces);

        for (Point pt : configuration.predefinedGoalPositions) {
            point.clear();
            point.put("x", pt.getX());
            point.put("y", pt.getY());
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
        Set<Position> tmp_positions = new HashSet<>();
        tmp_positions = this.board.getPiecesPosition();
        Position placed = this.board.generatePiece(r.nextDouble());
        tmp_positions.add(placed);
        this.board.setPiecesPosition(tmp_positions);
        this.board.getCellsGrid()[placed.getX()][ placed.getY()].cellState = CellState.Piece;
    }

    public void printBoard()
    {
        int col = this.board.getBoardWidth();
        int row = this.board.getBoardHeight();
        int task = this.board.getTaskAreaHeight();
        int goal = this.board.getGoalAreaHeight();
        Set<Position> positions = this.board.getPiecesPosition();
        String fld;
        CellState cState;

        for (int i = 0; i < row; i++)
        {
            if (i < goal || i > goal + task - 1)
                fld = "G";
            else
                fld = "T";
            System.out.println(" " + "######".repeat(col) + "#");
            System.out.println(" " + "|     ".repeat(col) + "|");
            for (int j = 0; j < col; j++)
            {
                cState = this.board.getCellsGrid()[j][i].cellState;
                if(cState == CellState.Piece || cState == CellState.Sham || cState == CellState.Valid)
                    fld += "|  P  ";
                else if(cState == CellState.Goal)
                    fld += "|  G  ";
                else if(cState == CellState.Unknown)
                    fld += "|  U  ";
                else if(cState == CellState.Empty)
                    fld += "|     ";

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