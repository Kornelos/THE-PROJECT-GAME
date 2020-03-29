package pl.mini.board;


import org.junit.Before;
import org.junit.Test;

public class GameMasterBoardTests {

    GameMasterBoard testGameMasterBoard;

    @Before
    public void prepareGameMasterBoardTests() {
        testGameMasterBoard = new GameMasterBoard(30, 15, 15);
    }

    @Test
    public void testGameMasterBoardPlayerMove_Correct() {
        // Check if move is valid
        // Check if player is moved
        // Check if appropriate Position calculation is made against known value
    }

    @Test
    public void testGameMasterBoardPlayerMove_Invalid() {
        // Check if move is valid
    }

    @Test
    public void testGameMasterBoardTakePiece_Correct() {
        // Check if piece is take-able
        // Check Add piece to player
        // Check Remove piece
    }

    @Test
    public void testGameMasterBoardTakePiece_Invalid() {
        // Check if piece is take-able
    }

    @Test
    public void testGameMasterBoardGeneratePiece_Correct() {
        // Check if cell is vacant
        // Check if piece is placed in vacant spot
    }

    @Test
    public void testGameMasterBoardGeneratePiece_Invalid() {
        // Check if cell is vacant
        // Check if failure on piece is placed in non vacant spot
    }

    @Test
    public void testGameMasterBoardSetGoal() {
        // Check goal is set at appropriate position
    }

    @Test
    public void testGameMasterBoardPlacePiece_Correct() {
        // Check if cell is vacant
        // Check if placed
    }

    @Test
    public void testGameMasterBoardPlacePiece_Invalid() {
        // Check if cell is vacant
        // Check if fail if placing in taken spot
        // Check if placing without owning a piece
    }

    @Test
    public void testGameMasterBoardPlacePlayer() {
        // Check if cell is valid
        // Check if player is placed
        // Check if player can move after placement
    }

    @Test
    public void testGameMasterBoardCheckWinCondition_Won() {
        // Check appropriate team win condition
    }

    @Test
    public void testGameMasterBoardCheckWinCondition_Lost() {
        // Check appropriate team win condition
    }

    @Test
    public void testGameMasterBoardDiscover() {
        // Check appropriate neighbor values are shown
        // Check if wall neighbors are shown as empty/ not showing anything
    }

}
