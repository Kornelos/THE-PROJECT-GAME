package pl.mini.messages;

import com.google.gson.JsonSyntaxException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
                return null;
            case move:
                return null;
            case test:
                return null;
            case place:
                return null;
            case start:
                return null;
            case pickup:
                return null;
            case discover:
                return null;
        }

        // if code hasn't returned any value throw exception
        throw new JsonSyntaxException("Incorrect Json argument");
    }
}
