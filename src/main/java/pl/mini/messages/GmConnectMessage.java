package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class GmConnectMessage implements JsonMessage {
    @Getter
    public MessageAction action = MessageAction.gmConnect;

    public GmConnectMessage() {
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
