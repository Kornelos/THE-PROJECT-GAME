package pl.mini.cell;

import lombok.Getter;
import lombok.Setter;
public class Field {
    @Getter @Setter private Position position;
    @Getter @Setter private Cell cell;
    public Field( Position position, Cell cell){
        this.cell = cell;
        this.position = position;
    }
}
