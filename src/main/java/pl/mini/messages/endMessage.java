package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class endMessage implements JsonMessage {
    @Getter
    private final MessageAction action;
    @Getter
    private final String result;

    public endMessage(MessageAction action, String result) {
        this.action = action;
        this.result = result;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("result", result);
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
