package pl.mini.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.mini.cell.*;
import pl.mini.position.Position;

public class BoardTests {

    Board testBoard;

    @Before
    public void prepareBoardTests() {
        testBoard = new Board(30, 15, 15);
    }

    @Test
    public void testBoardGetField() {
        // Check if Field object is returned

        Position testPos = new Position(3,4);
        Field test = testBoard.getField(testPos);
        Assert.assertNotNull(test);

        Position testPos2 = new Position(9, 2);
        Field test2 = testBoard.getField(testPos2);
        Assert.assertNotNull(test2);
    }

    @Test
    public void testBoardUpdateField() {
        // Check if Field is updated in board
        Cell testCell = new Cell(CellState.Piece);
        Position testPos3 = new Position(5,6);
        Field testField2 = new Field(testPos3, testCell);
        testBoard.updateField(testField2);
        Field testField3 = testBoard.getField(testPos3);
        Assert.assertEquals(testField2.getCell().cellState, testField3.getCell().cellState);

        Cell testCell2 = new Cell(CellState.Sham);
        Position testPos4 = new Position(10,10);
        Field testField4 = new Field(testPos4, testCell2);
        testBoard.updateField(testField4);
        Field testField5 = testBoard.getField(testPos4);
        Assert.assertEquals(testField4.getCell().cellState, testField5.getCell().cellState);
    }

    @Test
    public void testBoardInitializeCellsGrid() {
        // Check if Board is initialized with known board for input
        Assert.assertNotNull(testBoard.getCellsGrid());
    }

}
