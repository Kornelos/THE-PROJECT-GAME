package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class gmConnectMessage implements JsonMessage {
    @Getter
    public MessageAction action;

    public gmConnectMessage(MessageAction action) {
        this.action = action;
    }

    @Override
    public String toJson() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
