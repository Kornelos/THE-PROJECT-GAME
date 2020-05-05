package pl.mini.messages;

import com.google.gson.JsonArray;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;
import pl.mini.cell.Field;
import pl.mini.position.Position;

import java.util.*;

@EqualsAndHashCode
public class DiscoverStatusMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.discover;
    private final UUID playerGuid;
    private final Position position;
    private final List<Field> fields;
    private final List<String> tmp = new ArrayList<>();

    public DiscoverStatusMessage(UUID playerGuid, Position position, List<Field> fields) {
        this.playerGuid = playerGuid;
        this.position = position;
        this.fields = fields;
        for (Field f : fields) {
            tmp.add(f.toJsonString());
        }
    }

    @Override
    public String toJsonString() {
        JsonArray jsonArray = new JsonArray();
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        jsonMap.put("position", position.toString());
        for(String s : tmp)
        {
            jsonArray.add(s);
        }
        jsonMap.put("fields", jsonArray.toString());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
}
