package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.mini.CommServerMockSingleton;
import pl.mini.board.Board;
import pl.mini.board.PlacementResult;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.messages.*;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

import java.io.Console;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
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
    public boolean up, down, left, right;
    private List<UUID> playerGuids;
    private final PlayerCommServer commServer = new PlayerCommServer();


    public Player(String playerName, Board board,  Team team) {
        super();
        this.playerName = playerName;
        this.team = team;
        playerTeamColor = team.getColor();
        piece = false;
        try {
            commServer.connect();
            String msg = commServer.sendMessage((new ConnectMessage(playerUuid)).toString());
            JsonMessage jmsg = MessageFactory.messageFromString(msg);
            if (jmsg.getClass() == StartMessage.class)
            {
                this.board = ((StartMessage) jmsg).getBoard();
                team.setColor(((StartMessage) jmsg).getTeamColor());
                position = ((StartMessage) jmsg).getPosition();
                playerGuids = ((StartMessage) jmsg).getTeamGuids();
                playerTeamRole = ((StartMessage) jmsg).getTeamRole();
            }
            else
                throw new ClassCastException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {

    }

    public void makeAction() throws Exception {
        //NOTE: this is a temporary player game logic
        String msg;
        if (piece) {
            // goes to base
            // places piece in the base
            int goalHeight = board.getGoalAreaHeight();
            if (team.getColor() == TeamColor.Blue) {
                if (position.getY() >= board.getBoardHeight() - goalHeight) {
                    if (board.getCellsGrid()[position.getX()][position.getY()].cellState == CellState.Unknown) {
                        placePiece();
                        log.debug(playerName + " placing piece at: " + position.toString());
                    }
                    else {
                        seekGoal();
                    }

                } else
                    seekGoal();
            } else {
                if (position.getY() < goalHeight) {
                    if (board.getCellsGrid()[position.getX()][position.getY()].cellState == CellState.Unknown) {
                        placePiece();
                        log.debug(playerName + " placing piece at: " + position.toString());
                    }
                    else {
                        seekGoal();
                    }
                } else
                    seekGoal();

            }

        } else {
            msg = this.commServer.sendMessage((new DiscoverMessage(playerUuid, position)).toString());
            DiscoverResultMessage drm = (DiscoverResultMessage) MessageFactory.messageFromString(msg);
            List<Field> fieldList = drm.getFields();
            Field minField = new Field(new Position(0,0), new Cell(CellState.Empty, "", Integer.MAX_VALUE));
            for (Field f : fieldList) {
                this.board.getCellsGrid()[f.getPosition().getX()][f.getPosition().getY()] = f.getCell();
                if(f.getCell().distance < minField.getCell().distance)
                    minField = f;
            }
            int dirX = minField.getPosition().getX() - this.position.getX();
            int dirY = minField.getPosition().getY() - this.position.getY();

            if (dirX < 0) { left=true; right=false; }
            else if (dirX > 0) { left = false; right = true; }
            else { left = false; right = false; }

            if (dirY < 0) { down=true; up=false; }
            else if (dirY > 0) { down = false; up = true; }
            else { down = false; up = false; }

            MoveResultMessage mrm;
            if (down || up) {
                if (down)
                    msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Down).toString());
                else
                    msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Up).toString());
                mrm = (MoveResultMessage) MessageFactory.messageFromString(msg);
                if(mrm.getStatus().toString().equals("OK"))
                    this.position = mrm.getPosition();
            }
            if (left || right) {
                if (left)
                    msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Left).toString());
                else
                    msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Right).toString());
                mrm = (MoveResultMessage) MessageFactory.messageFromString(msg);
                if(mrm.getStatus().toString().equals("OK"))
                    this.position = mrm.getPosition();
            }
            if (board.getCellsGrid()[position.getX()][position.getY()].distance == 0 ||
                    board.getCellsGrid()[position.getX()][position.getY()].getCellState() == CellState.Piece) {
                msg = commServer.sendMessage( new TestMessage(playerUuid).toString());
                TestStatusMessage tsm = (TestStatusMessage) MessageFactory.messageFromString(msg);
                if(tsm.getStatus().toString().equals("OK"))
                {
                    msg = commServer.sendMessage(new PickupMessage(playerUuid).toString());
                    PickupResultMessage prm = (PickupResultMessage) MessageFactory.messageFromString(msg);
                    if(prm.getResult().equals("OK"))
                    {
                        board.getCellsGrid()[position.getX()][position.getY()].setCellState(CellState.Empty);
                        piece = true;
                    }
                }
            }
        }

        /*else if (askForMDist() != -1) {
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
                } else if (askForMDist() == mDist) {
                    move(Direction.Down);
                    if (askForMDist() > mDist) {
                        move(Direction.Up);
                        vertical = false;
                    }
                }
            }

            mDist = askForMDist();
            if (horizontal) {
                move(Direction.Right);
                if (askForMDist() > mDist) {
                    move(Direction.Left);
                    move(Direction.Left);
                    if (askForMDist() > mDist) {
                        move(Direction.Right);
                        horizontal = false;
                    } else if (askForMDist() == mDist)
                        horizontal = false;
                } else if (askForMDist() == mDist) {
                    move(Direction.Left);
                    if (askForMDist() > mDist) {
                        move(Direction.Right);
                        horizontal = false;
                    }
                }
            }
            log.debug(playerName + " distance to piece: " + mDist);
            // proceed to the target
            if (askForMDist() == 0) {
                log.debug(playerName + "taking piece at: " + position.toString());
                piece = takePiece();
                if (piece) {
                    vertical = true;
                    horizontal = true;
                }
            }


        }*/
        log.debug("Player " + playerName + " location:" + position.toString());
    }

    private List<Field> discover() {
        // scan local area
        return CommServerMockSingleton.INSTANCE.requestDiscover(this);
    }

    private int askForMDist() {
        // asks for manhattan distance
        return CommServerMockSingleton.INSTANCE.requestClosestPieceManhattan(this);
    }

    private int askForMDistToUnknown() {
        return CommServerMockSingleton.INSTANCE.requestClosestUnknownManhattan(this);
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
    }

    private void seekGoal() {
    }

    private static void sendMessage(String jsonObject) {
        String msg = jsonObject;


    }

    public static void main(String[] args) {
        try {
            System.out.println("OK".equals(Status.OK.toString()));
            PlayerCommServer communicationServer = new PlayerCommServer();
            communicationServer.connect();
            Thread.sleep(5000);
            String msg = communicationServer.sendMessage("This is a message from external class");
            System.out.println("===================================" + msg + "========================================");
            Thread.sleep(5000);
            // communicationServer.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
