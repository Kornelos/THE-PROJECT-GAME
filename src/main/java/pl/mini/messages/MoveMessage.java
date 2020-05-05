package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;
import pl.mini.position.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class MoveMessage implements JsonMessage{
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action = MessageAction.move;
    @Getter
    private final Direction direction;

    public MoveMessage(UUID pGUID, Direction dir){
        this.playerGuid = pGUID;
        this.direction = dir;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", this.playerGuid.toString());
        jsonMap.put("action", this.action.name());
        jsonMap.put("direction", this.direction.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

    @Override
    public String getTarget() {
        return "gm";
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}