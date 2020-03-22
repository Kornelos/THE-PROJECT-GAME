package pl.mini.cell;

import java.time.Duration;

public class Cell{
    public enum CellState {
        Empty,
        Goal,
        Piece,
        Sham,
        Valid,
        Unknown
    }
    public CellState cellstate;
    public Duration generationTimestamp;
    public Cell(CellState cellstate){
        this.cellstate = cellstate;
    }
}
