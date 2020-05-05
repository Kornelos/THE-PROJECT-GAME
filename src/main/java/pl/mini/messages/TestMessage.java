package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class TestMessage implements JsonMessage{
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action = MessageAction.test;

    public TestMessage(UUID pGUID){
        this.playerGuid = pGUID;
    }

    @Override
    public String toJsonString(){
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid",this.playerGuid.toString());
        jsonMap.put("action", this.action.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
    @Override
    public String toString(){
        return toJsonString();
    }
}