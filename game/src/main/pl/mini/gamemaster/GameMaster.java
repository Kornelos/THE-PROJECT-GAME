package pl.mini.gamemaster;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.mini.board.GameMasterBoard;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameMaster {
    private int portNumber;
    private InetAddress ipAddress;
    private List<UUID> teamRedGuids;
    private List<UUID> teamBlueGuids;

    @Getter
    @Setter
    private GameMasterBoard board;

    @Getter
    @Setter
    private GameMasterStatus status;

    private GameMasterConfiguration configuration;

    public GameMasterConfiguration getConfiguration(){
        return configuration;
    }

    public void setConfiguration(GameMasterConfiguration gc){
        configuration = gc;
    }

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
            finalConf.maxTeamSize = (int)conf.get("maxTeamSize");
            finalConf.maxPieces = (int)conf.get("maxPieces");

            JSONArray pointList = (JSONArray) conf.get("predefinedGoalPositions");
            for(Object obj : pointList){
                if(obj instanceof JSONObject){
                    tmp = new Point();
                    tmp.x = (int)((JSONObject) obj).get("x");
                    tmp.y = (int)((JSONObject) obj).get("y");
                    points.add(tmp);
                }
            }
            finalConf.predefinedGoalPositions = new Point[points.size()];
            points.toArray(finalConf.predefinedGoalPositions);

            finalConf.boardWidth = (int)conf.get("boardWidth");
            finalConf.boardTaskHeight = (int)conf.get("boardTaskHeight");
            finalConf.boardGoalHeight = (int)conf.get("boardGoalHeight");
            finalConf.delayDestroyPiece = (int)conf.get("delayDestroyPiece");
            finalConf.delayNextPiecePlace = (int)conf.get("delayNextPiecePlace");
            finalConf.delayMove = (int)conf.get("delayMove");
            finalConf.delayDiscover = (int)conf.get("delayDiscover");
            finalConf.delayTest = (int)conf.get("delayTest");
            finalConf.delayPick = (int)conf.get("delayPick");
            finalConf.delayPlace = (int)conf.get("delayPlace");

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

    private void putNewPiece()
    {

    }

    private void printBoard()
    {

    }

    public void messageHandler(String message)
    {

    }
}