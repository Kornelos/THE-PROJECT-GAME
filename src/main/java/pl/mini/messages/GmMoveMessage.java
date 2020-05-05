package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.mini.position.Direction;
import pl.mini.position.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class GmMoveMessage implements JsonMessage{
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action = MessageAction.move;
    @Getter
    private final Direction direction;
    @Getter
    private final Position position;
    @Getter
    private final Status status;

    public GmMoveMessage(UUID pGUID, Direction dir, Position pos, Status stat){
        this.playerGuid = pGUID;
        this.direction = dir;
        this.position = pos;
        this.status = stat;
    }

    @Override
    public String toJsonString(){
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid",this.playerGuid.toString());
        jsonMap.put("action", this.action.name());
        jsonMap.put("direction",this.direction.name());
        JSONObject point = new JSONObject();
        point.put("x", this.position.getX());
        point.put("y", this.position.getY());
        JSONArray position = new JSONArray();
        position.add(point);
        JSONObject json = new JSONObject(jsonMap);
        json.put("position",point);
        return json.toString();
    }
    @Override
    public String toString(){
        return toJsonString();
    }
}