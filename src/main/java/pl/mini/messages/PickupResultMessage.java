package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PickupResultMessage implements JsonMessage {
    @Getter
    private final MessageAction action;
    @Getter
    private final UUID playerGuid;
    @Getter
    private final String result;

    public PickupResultMessage(MessageAction action, UUID playerGuid, String result) {
        this.action = action;
        this.playerGuid = playerGuid;
        this.result = result;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("result", result);
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
