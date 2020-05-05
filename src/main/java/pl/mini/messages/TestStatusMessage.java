package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONObject;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
public class TestStatusMessage implements JsonMessage{
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action = MessageAction.test;
    @Getter
    private final Status status;
    @Getter
    private Test test;

    public TestStatusMessage(UUID pGUID, Status stat, Test tst){
        this.playerGuid = pGUID;
        this.status = stat;
        this.test = tst;
    }

    @Override
    public String toJsonString(){
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("playerGuid",this.playerGuid.toString());
        jsonMap.put("action", this.action.name());
        jsonMap.put("status", this.status.name());
        jsonMap.put("test", this.test.name());
        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }
    @Override
    public String toString(){
        return toJsonString();
    }
}