package pl.mini.cell;

import pl.mini.position.Position;
import lombok.Getter;
import lombok.Setter;
public class Field {
    @Getter @Setter private Position position;
    @Getter @Setter private Cell cell;
    @Getter @Setter private FieldColor fieldColor;
    public Field( Position position, Cell cell){
        this.cell = cell;
        this.position = position;
    }
}
