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
    boolean up = true, left = true, right = true, down = true;
    boolean isFirstAction = true;
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
            //restart
            boolean up = true, left = true, right = true, down = true;
            isFirstAction = true;
            // goes to base (how?)
            // places piece in the base
            Direction baseDirection;
            int goalHeight = board.getGoalAreaHeight();
            if (team.getColor() == TeamColor.Blue) {
                baseDirection = Direction.Down;
                if (position.getY() > board.getBoardHeight() - goalHeight)
                    placePiece();
                else
                    move(baseDirection);
            } else {
                baseDirection = Direction.Up;
                if (position.getY() < goalHeight)
                    placePiece();
                else
                    move(baseDirection);
            }

        } else {
            int mDist = askForMDist();
            // looks for piece
            // ask for manhattan distance

            if (vertical) {
                move(Direction.Up);
                if (askForMDist() > mDist) {
                    move(Direction.Down);
                    move(Direction.Down);
                    if(askForMDist() > mDist) {
                        move(Direction.Up);
                        vertical = false;
                    }
                    else if(askForMDist() == mDist)
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
                }
                else if(askForMDist() == mDist) {
                    move(Direction.Left);
                    if(askForMDist() > mDist) {
                        move(Direction.Right);
                        horizontal = false;
                    }
                }
            }
            System.out.println(" distance to piece: " + mDist);
            // proceed to the target
            if (!horizontal && !vertical)
                piece = takePiece();


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
            // TODO: update player board
        }
        piece = false;
    }


}
