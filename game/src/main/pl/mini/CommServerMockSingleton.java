package pl.mini;

import pl.mini.board.GameMasterBoard;
import pl.mini.board.PlacementResult;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.gamemaster.GameMaster;
import pl.mini.player.Player;
import pl.mini.position.Direction;
import pl.mini.position.Position;

import java.util.List;

/**
 * This is a temporary mockup of communication server
 */
public class CommServerMockSingleton {
    public static final CommServerMockSingleton INSTANCE = new CommServerMockSingleton();

    public GameMaster gameMaster = null;

    private CommServerMockSingleton() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Singleton already constructed");
        }
    }

    public void registerGameMaster(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }

    public Position requestPlayerMove(Player player, Direction direction) {
        GameMasterBoard gmb = gameMaster.getBoard();
        return gmb.playerMove(player, direction);
    }

    public boolean requestPlayerPickPiece(Player player) {
        GameMasterBoard gmb = gameMaster.getBoard();
        if (gmb.getField(player.getPosition()).getCell().getCellState() == CellState.Piece) {
            gmb.takePiece(player.getPosition());
            return true;
        } else {
            return false;
        }

    }

    public CellState requestPieceTest(Player player) {
        GameMasterBoard gmb = gameMaster.getBoard();
        List<Field> nearFields = gmb.discover(player.getPosition());

        for (Field field : nearFields) {
            CellState cs = field.getCell().getCellState();
            if (cs == CellState.Piece) {
                return CellState.Piece;

            }
        }
        return null;
    }

    public List<Field> requestDiscover(Player player) {
        GameMasterBoard gmb = gameMaster.getBoard();
        return gmb.discover(player.getPosition());
    }

    public int requestClosestPieceManhattan(Player player) {
        GameMasterBoard gmb = gameMaster.getBoard();
        return gmb.manhattanDistanceToClosestPiece(player.getPosition());
    }

    public PlacementResult requestPlacePiece(Player player) {
        GameMasterBoard gmb = gameMaster.getBoard();
        return gmb.placePiece(player, gameMaster.getConfiguration().shamProbability);
    }


}
