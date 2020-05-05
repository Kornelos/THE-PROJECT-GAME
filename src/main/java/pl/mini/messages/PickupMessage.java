package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PickupMessage implements JsonMessage {
    @Getter
    private final MessageAction action;
    @Getter
    private final UUID playerGuid;

    public PickupMessage(MessageAction action, UUID playerGuid) {
        this.action = action;
        this.playerGuid = playerGuid;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
