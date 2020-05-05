package pl.mini.cell;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import pl.mini.position.Position;

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



    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", position.getX());
        jsonObject.put("y", position.getY());
        jsonObject.put("cell", cell.toJSONObject());

        return  jsonObject;
    }


}
