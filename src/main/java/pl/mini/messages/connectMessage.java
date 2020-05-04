package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class connectMessage implements JsonMessage {
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action;

    public connectMessage(UUID playerGuid, MessageAction action) {
        this.action = action;
        this.playerGuid = playerGuid;
    }

    @Override
    public String toJson() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
