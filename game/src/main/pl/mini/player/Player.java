package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import pl.mini.CommServerMockSingleton;
import pl.mini.board.Board;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.cell.FieldColor;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

import java.net.InetAddress;
import java.util.List;

public class Player extends PlayerDTO {
    @Getter
    private final String playerName;
    private Board board;
    @Getter
    @Setter
    private Team team;
    private ActionType lastAction;
    private Direction lastDirection;
    private boolean piece;
    private Position position;
    private PlayerState playerState;
    private int portNumber;
    private InetAddress ipAddress;

    public Player(String playerName) {
        super();
        this.playerName = playerName;
        piece = false;
    }

    public void listen() {

    }

    public void makeAction() {
        if (piece) {
            // goes to base (how?)
            // TODO: figure out how player should know where his goal area is
            // places piece in the base
            Direction baseDirection;
            if (team.getColor() == TeamColor.Blue)
                baseDirection = Direction.Down;
            else
                baseDirection = Direction.Up;
            while (true) {
                List<Field> fieldList = discover();
                if (((fieldList.get(4).getFieldColor() == FieldColor.Blue) && (team.getColor() == TeamColor.Blue)) || ((fieldList.get(4).getFieldColor() == FieldColor.Red) && (team.getColor() == TeamColor.Red))) {
                    placePiece();
                    break;
                } else move(baseDirection);
            }
        } else {
            // looks for piece
            // ask for manhattan distance
            int mDist = askForMDist();
            int initMDist = mDist;
            boolean up = true, left = true, right = true, down = true;
            while (mDist != 1) {
                if (up) {
                    move(Direction.Up);
                    mDist = askForMDist();
                    if (mDist >= initMDist) {
                        up = false;
                    }
                    continue;
                }
                if (down) {
                    move(Direction.Down);
                    mDist = askForMDist();
                    if (mDist >= initMDist) {
                        down = false;
                    }
                    continue;
                }
                if (left) {
                    move(Direction.Left);
                    mDist = askForMDist();
                    if (mDist >= initMDist) {
                        left = false;
                    }
                    continue;
                }
                if (right) {
                    move(Direction.Up);
                    mDist = askForMDist();
                    if (mDist >= initMDist) {
                        right = false;
                    }
                }
            }
            // proceed to the target


            // if mdist == 1
            // test piece
            // grab piece or not if sham
            if (testPiece())
                piece = takePiece();
        }
    }

    private List<Field> discover() {
        // scan local area
        return CommServerMockSingleton.INSTANCE.requestDiscover(this);
    }

    private int askForMDist() {
        // asks for manhattan distance
        return 0;

    }

    private void move(Direction direction) {
        CommServerMockSingleton.INSTANCE.requestPlayerMove(this, direction);
    }

    private boolean takePiece() {
        return CommServerMockSingleton.INSTANCE.requestPlayerPickPiece(this);
    }

    private boolean testPiece() {
        return CommServerMockSingleton.INSTANCE.requestPieceTest(this) == CellState.Piece;
    }

    private void placePiece() {
        CommServerMockSingleton.INSTANCE.requestPlacePiece(this);
        piece = false;
    }


}
