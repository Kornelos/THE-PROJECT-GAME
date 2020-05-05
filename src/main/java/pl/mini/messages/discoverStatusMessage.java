package pl.mini.messages;

import lombok.Getter;
import org.json.simple.JSONObject;
import pl.mini.cell.Field;
import pl.mini.position.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public class discoverStatusMessage implements JsonMessage {
    @Getter
    private final MessageAction action;
    private final UUID playerGuid;
    private final Position position;
    private final List<Field> fields;

    public discoverStatusMessage(UUID playerGuid, MessageAction action, Position position, List<Field> fields)
    {
        this.action = action;
        this.playerGuid = playerGuid;
        this.position = position;
        this.fields = fields;
    }

    @Override
    public String toJson() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        jsonMap.put("position", position.toString());
        jsonMap.put("fields", fields.toString());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
