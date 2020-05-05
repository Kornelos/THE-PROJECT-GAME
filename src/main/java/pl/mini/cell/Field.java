package pl.mini.cell;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import pl.mini.position.Position;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        return toJsonString();
    }


}
