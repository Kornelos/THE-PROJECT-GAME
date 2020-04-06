package pl.mini.cell;


import lombok.Getter;
import lombok.Setter;

public class Cell {
    @Getter
    @Setter
    public CellState cellState;
    public int distance;
    public String playerGuids;

    public Cell(CellState cellState) {
        this.cellState = cellState;
        this.playerGuids = "";
    }
}
