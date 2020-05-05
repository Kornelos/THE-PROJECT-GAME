package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class EndMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.pickup;
    @Getter
    private final String result;

    public EndMessage(String result) {
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

    @Override
    public String toString() { return toJsonString(); }
}
