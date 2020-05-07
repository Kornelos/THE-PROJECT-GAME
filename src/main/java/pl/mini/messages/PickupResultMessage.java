package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class PickupResultMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.pickupResult;
    @Getter
    private final UUID playerGuid;
    @Getter
    private final Status status;

    public PickupResultMessage(UUID playerGuid, Status status) {
        this.playerGuid = playerGuid;
        this.status = status;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("status", status.name());
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

    @Override
    public String getTarget() {
        return playerGuid.toString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
