package pl.mini.messages;

import com.google.gson.JsonSyntaxException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.mini.board.Board;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;
import pl.mini.position.Position;

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
    public static JsonMessage messageFromString(String jsonString) throws ParseException, JsonSyntaxException {
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
            case testStatus:
                return new TestStatusMessage(UUID.fromString((String) json.get("playerGuid")),
                        Status.valueOf((String) json.get("status")),
                        Test.valueOf((String) json.get("test")));
            case place:
                return new PlaceMessage(UUID.fromString((String) json.get("playerGuid")));
            case start:
                JSONArray pointList = (JSONArray) json.get("teamGuids");
                List<UUID> guids = new ArrayList<>();
                JSONObject pos = (JSONObject) json.get("position");
                JSONObject brd = (JSONObject) json.get("board");
                for (Object o : pointList)
                    guids.add(UUID.fromString(o.toString()));
                return new StartMessage(TeamColor.valueOf((String) json.get("teamColor")),
                        TeamRole.valueOf((String) json.get("teamRole")),
                        (int) json.get("teamSize"),
                        guids,
                        new Position((int) pos.get("x"), (int) pos.get("y")),
                        new Board((int) brd.get("boardWidth"), (int) brd.get("goalAreaHeight"), (int) brd.get("taskAreaHeight")));
            case pickup:
                return new PickupMessage(UUID.fromString((String) json.get("playerGuid")));
            case pickupResult:
                return new PickupResultMessage(UUID.fromString((String) json.get("playerGuid")), (String) json.get("result"));
            case discover:
                int x = (int) json.get("x");
                int y = (int) json.get("y");
                Position position = new Position(x, y);
                return new DiscoverMessage(UUID.fromString((String) json.get("playerGuid")), position);
        }

        // if code hasn't returned any value throw exception
        throw new JsonSyntaxException("Incorrect Json argument");
    }
}
