package pl.mini.cell;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import pl.mini.position.Position;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
public class Field {
    @Getter
    @Setter
    private Position position;
    @Getter
    @Setter
    private Cell cell;
    @Getter
    @Setter
    private FieldColor fieldColor;

    public Field(Position position, Cell cell) {
        this.cell = cell;
        this.position = position;
    }

    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("x", String.valueOf(position.getX()));
        jsonMap.put("y", String.valueOf(position.getY()));
        jsonMap.put("cell", cell.toString());

        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", position.getX());
        jsonObject.put("y", position.getY());
        jsonObject.put("cell", cell.toJSONObject());

        return  jsonObject;
    }

    @Override
    public String toString() {
        return toJsonString();
    }


}
