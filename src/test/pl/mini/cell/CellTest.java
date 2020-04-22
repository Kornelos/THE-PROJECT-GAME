package pl.mini.cell;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CellTest {
    private Cell c;
    @Before
    public void initCellTests() {
        c = new Cell(CellState.Piece);
    }
    @Test
    public void testCreate(){
        Assert.assertNotNull(c.cellState);
    }
}
