package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceResultMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.placeResult;
    @Getter
    private final UUID playerGuid;
    @Getter
    private final PlacementResult result;
    @Getter
    private final Status status;

    public PlaceResultMessage(UUID playerGuid, PlacementResult result, Status status) {
        this.playerGuid = playerGuid;
        this.result = result;
        this.status = status;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("placementResult", this.result.name());
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        jsonMap.put("status", this.status.name());
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