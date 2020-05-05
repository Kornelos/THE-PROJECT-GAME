package pl.mini.messages;

import com.google.gson.JsonArray;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.mini.cell.Field;
import pl.mini.position.Position;

import java.util.*;

@EqualsAndHashCode
public class DiscoverResultMessage implements JsonMessage {
    @Getter
    private final MessageAction action = MessageAction.discoverResult;
    @Getter
    private final UUID playerGuid;
    @Getter
    private final Position position;
    @Getter
    private final List<Field> fields;

    public DiscoverResultMessage(UUID playerGuid, Position position, List<Field> fields) {
        this.playerGuid = playerGuid;
        this.position = position;
        this.fields = fields;
    }

    @Override
    public String toJsonString() {
        JsonArray jsonArray = new JsonArray();
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
        JSONObject point = new JSONObject();
        JSONObject json = new JSONObject(jsonMap);
        point.put("x", this.position.getX());
        point.put("y", this.position.getY());
        json.put("position",point);
        JSONArray fields = new JSONArray();
        for(Field f : this.fields)
        {
            fields.add(f.toJSONObject());
        }
        json.put("fields", fields);
        return json.toString();
    }

    @Override
    public String toString(){
        return this.toJsonString();
    }
}
