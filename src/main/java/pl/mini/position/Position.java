package pl.mini.position;

import lombok.*;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Position {
    @Getter
    @Setter
    private int x;
    @Getter
    @Setter
    private int y;

    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("x", String.valueOf(x));
        jsonMap.put("y", String.valueOf(y));

        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}