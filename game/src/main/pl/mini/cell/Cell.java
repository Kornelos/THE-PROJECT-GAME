package pl.mini.cell;


public class Cell{
    public CellState cellState;
    public int distance;
    public String playerGuids;
    public Cell(CellState cellState){
        this.cellState = cellState;
    }
}
