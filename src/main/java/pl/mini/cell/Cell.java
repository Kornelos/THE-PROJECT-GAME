package pl.mini.cell;


import lombok.Getter;
import lombok.Setter;

public class Cell {
    @Getter
    @Setter
    public CellState cellState;
    public int distance = 0;
    public String playerGuids;

    public Cell(CellState cellState) {
        this.cellState = cellState;
        this.playerGuids = "";
    }

    @Override
    public String toString()
    {
        return String.format("cellState: " + cellState + "distance: " + distance + "playerGuids: " + playerGuids);
    }
}
