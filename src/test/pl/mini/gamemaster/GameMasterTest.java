package pl.mini.gamemaster;

import org.junit.Assert;
import org.junit.Test;
import pl.mini.board.GameMasterBoard;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.position.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameMasterTest
{

    @Test
    public void testLoadAndSaveToJSON() throws IOException {
        GameMaster gm = new GameMaster();
        GameMasterConfiguration gc1 = new GameMasterConfiguration(0.8,6,7, new Position[]{new Position(3, 4), new Position(5, 6), new Position(7, 8)},8,
                                                                  6, 2, 2, 1, 1, 2, 3, 5, 2);

        gm.setConfiguration(gc1);
        gm.saveConfigurationToJson("./src/test/pl/mini/TestJSONs/test1b.json");
        GameMaster gm2 = new GameMaster();
        gm2.setConfiguration(gm2.loadConfigurationFromJson("./src/test/pl/mini/TestJSONs/test1b.json"));
        Assert.assertEquals(gm.getConfiguration().toString(), gm2.getConfiguration().toString());
        GameMaster gm3 = new GameMaster();
        GameMasterConfiguration gc3 = new GameMasterConfiguration(0.4,4,8, new Position[]{new Position(1, 2), new Position(3, 4), new Position(5, 6)},10,
                                                                  7, 3, 3, 2, 2, 3, 4, 2, 3);
        gm3.setConfiguration(gc3);
        gm3.saveConfigurationToJson("./src/test/pl/mini/TestJSONs/test2b.json");
        GameMaster gm4 = new GameMaster();
        gm4.setConfiguration(gm4.loadConfigurationFromJson("./src/test/pl/mini/TestJSONs/test2b.json"));
        Assert.assertEquals(gm3.getConfiguration().toString(), gm4.getConfiguration().toString());

    }

    @Test
    public void testPutNewPieceAndTakePiece()
    {
        GameMaster gm = new GameMaster();
        GameMasterBoard board = new GameMasterBoard(6,2,4);
        gm.setBoard(board);
        GameMaster gm2 = new GameMaster();
        gm2.setBoard(board);
        Position position = new Position(0,2);
        gm.putNewPiece(position);
        gm2.getBoard().getCellsGrid()[0][2].setCellState(CellState.Piece);
        for(int i = board.getGoalAreaHeight(); i < board.getGoalAreaHeight() + board.getTaskAreaHeight(); i++)
        {
            for(int j = 0; j < board.getBoardWidth(); j++)
            {
                Position pos = new Position(j, i);
                gm2.getBoard().getCellsGrid()[j][i].distance = i - 2 + j;
            }
        }
        Assert.assertArrayEquals(gm2.getBoard().getCellsGrid(), gm.getBoard().getCellsGrid());
        Position position2 = new Position(5,2);
        gm.putNewPiece(position2);
        gm.getBoard().getCellsGrid()[position2.getX()][position2.getY()].setCellState(gm.getBoard().takePiece(position2));
        Assert.assertArrayEquals(gm2.getBoard().getCellsGrid(), gm.getBoard().getCellsGrid());
    }



}
