package pl.mini.cell;

import lombok.Getter;
import lombok.Setter;
import pl.mini.position.Position;

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
}
