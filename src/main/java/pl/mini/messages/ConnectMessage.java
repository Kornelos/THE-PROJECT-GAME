package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class ConnectMessage implements JsonMessage {
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action = MessageAction.connect;

    public ConnectMessage(UUID playerGuid) {
        this.playerGuid = playerGuid;
    }

    @Override
    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid", playerGuid.toString());
        jsonMap.put("action", action.name());
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
