package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;
import pl.mini.position.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class DiscoverMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.discover;
    @Getter
    private final UUID playerGuid;
    @Getter
    private final Position position;

    public DiscoverMessage(UUID playerGuid, Position position) {
        this.playerGuid = playerGuid;
        this.position = position;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        jsonMap.put("position", position.toString());
        JSONObject point = new JSONObject();
        JSONObject json = new JSONObject(jsonMap);
        point.put("x", this.position.getX());
        point.put("y", this.position.getY());
        json.put("position", point);
        return json.toString();
    }

    @Override
    public String getTarget() {
        return "gm";
    }

    @Override
    public String toString() {
        return this.toJsonString();
    }
}
