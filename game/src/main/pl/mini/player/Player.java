package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import pl.mini.CommServerMockSingleton;
import pl.mini.board.Board;
import pl.mini.board.PlacementResult;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

import java.net.InetAddress;
import java.util.List;

public class Player extends PlayerDTO {
    @Getter
    private final String playerName;
    private final Board board;
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
    boolean vertical = true, horizontal = true;
    int initMDist;

    public Player(String playerName, Board board, Team team) {
        super();
        this.playerName = playerName;
        this.board = board;
        this.team = team;
        playerTeamColor = team.getColor();
        piece = false;
        initMDist = -1;
    }

    public void listen() {

    }

    public void makeAction() {
        //NOTE: this is a temporary player game logic
        if (piece) {
            // goes to base (how?)
            // places piece in the base
            Direction baseDirection;
            int goalHeight = board.getGoalAreaHeight();
            if (team.getColor() == TeamColor.Blue) {
                baseDirection = Direction.Down;
                if (position.getY() >= board.getBoardHeight() - goalHeight
                        && position.getY() < board.getGoalAreaHeight()) {
                    placePiece();
                    System.out.println(playerName + " placing piece at: " + position.toString());
                } else
                    move(baseDirection);
            } else {
                baseDirection = Direction.Up;
                if (position.getY() < goalHeight) {
                    placePiece();
                    System.out.println(playerName + " placing piece at: " + position.toString());
                } else
                    move(baseDirection);

            }

        } else if(askForMDist() != -1) {
            int mDist = askForMDist();
            // looks for piece
            // ask for manhattan distance
            if (!horizontal && !vertical) {
                horizontal = true;
                vertical = true;
            }
            if (vertical) {
                move(Direction.Up);
                if (askForMDist() > mDist) {
                    move(Direction.Down);
                    move(Direction.Down);
                    if (askForMDist() > mDist) {
                        move(Direction.Up);
                        vertical = false;
                    } else if (askForMDist() == mDist)
                        vertical = false;
                }
                else if(askForMDist() == mDist) {
                    move(Direction.Down);
                    if (askForMDist() > mDist) {
                        move(Direction.Up);
                        horizontal = false;
                    }
                }
            }

            mDist = askForMDist();
            if (horizontal)
            {
                move(Direction.Right);
                if(askForMDist() > mDist)
                {
                    move(Direction.Left);
                    move(Direction.Left);
                    if(askForMDist() > mDist) {
                        move(Direction.Right);
                        horizontal = false;
                    }
                    else if(askForMDist() == mDist)
                        horizontal = false;
                } else if (askForMDist() == mDist) {
                    move(Direction.Left);
                    if (askForMDist() > mDist) {
                        move(Direction.Right);
                        horizontal = false;
                    }
                }
            }
            System.out.println(playerName + " distance to piece: " + mDist);
            // proceed to the target
            if (askForMDist() == 0) {
                System.out.println(playerName + "taking piece at: " + position.toString());
                piece = takePiece();
            }


        }
        System.out.println("Player " + playerName + " location:" + position.toString());
    }

    private List<Field> discover() {
        // scan local area
        return CommServerMockSingleton.INSTANCE.requestDiscover(this);
    }

    private int askForMDist() {
        // asks for manhattan distance
        return CommServerMockSingleton.INSTANCE.requestClosestPieceManhattan(this);
    }

    private void move(Direction direction) {
        position = CommServerMockSingleton.INSTANCE.requestPlayerMove(this, direction);
    }

    private boolean takePiece() {
        return CommServerMockSingleton.INSTANCE.requestPlayerPickPiece(this);
    }

    private boolean testPiece() {
        return CommServerMockSingleton.INSTANCE.requestPieceTest(this) == CellState.Piece;
    }

    private void placePiece() {
        PlacementResult placementResult = CommServerMockSingleton.INSTANCE.requestPlacePiece(this);
        if (placementResult == PlacementResult.Correct) {
            board.getCellsGrid()[position.getX()][position.getY()].cellState = CellState.Valid;
        } else if (placementResult == PlacementResult.Pointless) {
            board.getCellsGrid()[position.getX()][position.getY()].cellState = CellState.Empty;
        }
        piece = false;
        vertical = true;
        horizontal = true;
    }


}
