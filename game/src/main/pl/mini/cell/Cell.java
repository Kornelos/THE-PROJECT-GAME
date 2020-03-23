package pl.mini.cell;

import java.time.Duration;

public class Cell{
    public CellState cellState;
    public Duration generationTimestamp;
    public Cell(CellState cellState){
        this.cellState = cellState;
    }
}
