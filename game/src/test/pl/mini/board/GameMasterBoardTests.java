package pl.mini.board;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.mini.cell.CellState;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Position;
import pl.mini.cell.Cell;
import pl.mini.team.TeamColor;

import java.util.HashSet;
import java.util.Set;

public class GameMasterBoardTests {

    GameMasterBoard testGameMasterBoard;

    @Before
    public void prepareGameMasterBoardTests() {
        testGameMasterBoard = new GameMasterBoard(30, 15, 15);
    }

    @Test
    public void testGameMasterBoardSetGoal() {
        // Check goal is set at appropriate position
        testGameMasterBoard.setGoal(new Position(6,6));
        Assert.assertEquals(testGameMasterBoard.getCellsGrid()[6][6].cellState, CellState.Goal);

        testGameMasterBoard.setGoal(new Position(9,8));
        Assert.assertEquals(testGameMasterBoard.getCellsGrid()[9][8].cellState, CellState.Goal);
    }

    @Test
    public void testGameMasterBoardPlacePiece_Correct() {
        PlayerDTO testDTO = new PlayerDTO();
        testDTO.setPosition(new Position(9,9));
        testGameMasterBoard.setGoal(new Position(9,9));
        Assert.assertEquals(testGameMasterBoard.placePiece(testDTO), PlacementResult.Correct);

        PlayerDTO testDTO2 = new PlayerDTO();
        testDTO2.setPosition(new Position(1,2));
        testGameMasterBoard.setGoal(new Position(1,2));
        Assert.assertEquals(testGameMasterBoard.placePiece(testDTO2), PlacementResult.Correct);
    }

    @Test
    public void testGameMasterBoardPlacePiece_Invalid() {
        PlayerDTO testDTO3 = new PlayerDTO();
        testDTO3.setPosition(new Position(7,6));
        Cell[][] temp = testGameMasterBoard.getCellsGrid();
        temp[7][6] = new Cell(CellState.Empty);
        testGameMasterBoard.setCellsGrid(temp);
        testGameMasterBoard.setGoal(new Position(9,9));
        Assert.assertEquals(testGameMasterBoard.placePiece(testDTO3), PlacementResult.Pointless);

        PlayerDTO testDTO4 = new PlayerDTO();
        testDTO4.setPosition(new Position(2,5));
        Cell[][] temp2 = testGameMasterBoard.getCellsGrid();
        temp2[2][5] = new Cell(CellState.Empty);
        testGameMasterBoard.setCellsGrid(temp2);
        testGameMasterBoard.setGoal(new Position(1,2));
        Assert.assertEquals(testGameMasterBoard.placePiece(testDTO4), PlacementResult.Pointless);
    }

    @Test
    public void testGameMasterBoardPlacePlayer() {
        PlayerDTO testDTO5 = new PlayerDTO();
        testDTO5.setPlayerTeamColor(TeamColor.Red);
        Assert.assertNotNull(testGameMasterBoard.placePlayer(testDTO5));

        PlayerDTO testDTO6 = new PlayerDTO();
        testDTO6.setPlayerTeamColor(TeamColor.Blue);
        Assert.assertNotNull(testGameMasterBoard.placePlayer(testDTO6));
    }


    @Test
    public void testGameMasterBoardManhattanDistanceTwoPoints() {
        // Check method result against known value
        Set<Position> testPositionSet = new HashSet<>();
        testPositionSet.add(new Position(3,5));
        testPositionSet.add(new Position(2,2));
        testGameMasterBoard.setPiecesPosition(testPositionSet);
        Assert.assertEquals(testGameMasterBoard.manhattanDistanceToClosestPiece(new Position(1,1)),6);
        Assert.assertEquals(testGameMasterBoard.manhattanDistanceToClosestPiece(new Position(3,3)),2);

    }

}
