package pl.mini;

import static org.junit.Assert.assertTrue;

import jdk.jfr.StackTrace;
import org.junit.Test;

public class AppTest 
{
    @Test
    public void testBoardGetField()
    {
        // Check if Field object is returned
        // Check if appropriate Field value is returned for known board
    }

    @Test
    public void testBoardUpdateField()
    {
        // Check if Field is updated in board
    }

    @Test
    public void testBoardUpdateCell()
    {
        // Check if cell is updated in board
    }

    @Test
    public void testBoardGetCell()
    {
        // Check if correct cell is retrieved from known board
        // Check if Cell object is returned
    }

    @Test
    public void testBoardInitializeCellsGrid()
    {
        // Check if Board is initialized with known board for input
    }

    @Test
    public void testBoardManhattanDistanceTwoPoints()
    {
        // Check method result against known value
    }

    @Test
    public void testGameMasterBoardPlayerMove_Correct()
    {
        // Check if move is valid
        // Check if player is moved
        // Check if appropriate Position calculation is made against known value
    }

    @Test
    public void testGameMasterBoardPlayerMove_Invalid()
    {
        // Check if move is valid
    }

    @Test
    public void testGameMasterBoardTakePiece_Correct()
    {
        // Check if piece is take-able
        // Check Add piece to player
        // Check Remove piece
    }

    @Test
    public void testGameMasterBoardTakePiece_Invalid()
    {
        // Check if piece is take-able
    }

    @Test
    public void testGameMasterBoardGeneratePiece_Correct()
    {
        // Check if cell is vacant
        // Check if piece is placed in vacant spot
    }

    @Test
    public void testGameMasterBoardGeneratePiece_Invalid()
    {
        // Check if cell is vacant
        // Check if failure on piece is placed in non vacant spot
    }

    @Test
    public void testGameMasterBoardSetGoal()
    {
        // Check goal is set at appropriate position
    }

    @Test
    public void testGameMasterBoardPlacePiece_Correct()
    {
        // Check if cell is vacant
        // Check if placed
    }

    @Test
    public void testGameMasterBoardPlacePiece_Invalid()
    {
        // Check if cell is vacant
        // Check if fail if placing in taken spot
        // Check if placing without owning a piece
    }

    @Test
    public void testGameMasterBoardPlacePlayer()
    {
        // Check if cell is valid
        // Check if player is placed
        // Check if player can move after placement
    }

    @Test
    public void testGameMasterBoardCheckWinCondition_Won()
    {
        // Check appropriate team win condition
    }

    @Test
    public void testGameMasterBoardCheckWinCondition_Lost()
    {
        // Check appropriate team win condition
    }

    @Test
    public void testGameMasterBoardDiscover()
    {
        // Check appropriate neighbor values are shown
        // Check if wall neighbors are shown as empty/ not showing anything
    }
}
