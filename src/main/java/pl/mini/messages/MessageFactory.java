package pl.mini.messages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.mini.board.Board;
import pl.mini.board.PlacementResult;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MessageFactory {
    /**
     * Factory for messages implementing JsonMessage interface
     *
     * @param jsonString json string
     * @return instance of JsonMessage
     * @throws ParseException if message is not correct JSON string
     */
    public static JsonMessage messageFromString(String jsonString) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);

        MessageAction action = MessageAction.valueOf((String) json.get("action"));

        switch (action) {
            case connect:
                return new ConnectMessage(UUID.fromString((String) json.get("playerGuid")));
            case gmConnect:
                return new GmConnectMessage();
            case end:
                return new EndMessage((String) json.get("result"));
            case move:
                return new MoveMessage(UUID.fromString((String) json.get("playerGuid")),
                        Direction.valueOf((String) json.get("direction")));
            case moveResult:
                JSONObject position = (JSONObject) json.get("position");
                return new MoveResultMessage(UUID.fromString((String) json.get("playerGuid")),
                        Direction.valueOf((String) json.get("direction")),
                        new Position((int) position.get("x"), (int) position.get("y")),
                        Status.valueOf((String) json.get("status")));
            case test:
                return new TestMessage(UUID.fromString((String) json.get("playerGuid")));
            case testResult:
                return new TestResultMessage(UUID.fromString((String) json.get("playerGuid")),
                        Status.valueOf((String) json.get("status")),
                        Test.valueOf((String) json.get("test")));
            case place:
                return new PlaceMessage(UUID.fromString((String) json.get("playerGuid")));
            case placeResult:
                return new PlaceResultMessage(UUID.fromString((String) json.get("playerGuid")),
                        PlacementResult.valueOf((String) json.get("placementResult"))
                        , Status.valueOf((String) json.get("status")));
            case start:
                JSONArray pointList = (JSONArray) json.get("teamGuids");
                List<UUID> guids = new ArrayList<>();
                JSONObject pos = (JSONObject) json.get("position");
                JSONObject brd = (JSONObject) json.get("board");
                for (Object o : pointList)
                    guids.add(UUID.fromString(o.toString()));
                return new StartMessage(UUID.fromString((String) json.get("playerGuid")),
                        TeamColor.valueOf((String) json.get("teamColor")),
                        TeamRole.valueOf((String) json.get("teamRole")),
                        ((Long) json.get("teamSize")).intValue(),
                        guids,
                        new Position(((Long) pos.get("x")).intValue(), ((Long) pos.get("y")).intValue()),
                        new Board(((Long) brd.get("boardWidth")).intValue(), ((Long) brd.get("goalAreaHeight")).intValue(),
                                ((Long) brd.get("taskAreaHeight")).intValue()));
            case pickup:
                return new PickupMessage(UUID.fromString((String) json.get("playerGuid")));
            case pickupResult:
                return new PickupResultMessage(UUID.fromString((String) json.get("playerGuid")), Status.valueOf((String) json.get("status")));
            case discover:
                JSONObject position1 = (JSONObject) json.get("position");
                int x = (int) position1.get("x");
                int y = (int) position1.get("y");
                Position discoverPosition = new Position(x, y);
                return new DiscoverMessage(UUID.fromString((String) json.get("playerGuid")), discoverPosition);
            case discoverResult:
                JSONObject pos1 = (JSONObject) json.get("position");
                Position discoverResultPosition = new Position(((Long) pos1.get("x")).intValue(),
                        ((Long) pos1.get("y")).intValue());
                List<Field> fields = new ArrayList<>();
                JSONArray fieldList = (JSONArray) json.get("fields");
                for (Object obj : fieldList) {
                    if (obj instanceof JSONObject) {
                        int xField = ((Long) ((JSONObject) obj).get("x")).intValue();
                        int yField = ((Long) ((JSONObject) obj).get("y")).intValue();
                        JSONObject cellJson = (JSONObject) ((JSONObject) obj).get("cell");
                        Cell cll = new Cell((CellState.valueOf((String) cellJson.get("cellState"))));
                        cll.playerGuid = (String) cellJson.get("playerGuid");
                        cll.distance = ((Long) cellJson.get("distance")).intValue();
                        fields.add(new Field(new Position(xField, yField), cll));
                    }
                }
                return new DiscoverResultMessage(UUID.fromString((String) json.get("playerGuid")),
                        discoverResultPosition, fields);

        }

        // if code hasn't returned any value throw exception
        throw new Exception("Incorrect Json argument");
    }
}
