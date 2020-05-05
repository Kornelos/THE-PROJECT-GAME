package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;
import pl.mini.position.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class discoverMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.discover;
    private final UUID playerGuid;
    private final Position position;

    public discoverMessage(UUID playerGuid, Position position)
    {
        this.playerGuid = playerGuid;
        this.position = position;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        jsonMap.put("position", position.toString());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
