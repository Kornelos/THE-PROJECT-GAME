package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.simple.JSONObject;
import pl.mini.CommServerMockSingleton;
import pl.mini.board.Board;
import pl.mini.board.PlacementResult;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.communication.CommunicationServer;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Slf4j
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
    public boolean vertical = true, horizontal = true;


    public Player(String playerName, Board board, Team team) {
        super();
        this.playerName = playerName;
        this.board = board;
        this.team = team;
        playerTeamColor = team.getColor();
        piece = false;
    }

    public void listen() {

    }

    public void makeAction() {
        //NOTE: this is a temporary player game logic
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

        } else if (askForMDist() != -1) {
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


        }
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
        vertical = true;
        horizontal = true;
    }

    private void seekGoal() {
        int mDist = askForMDistToUnknown();

        if (mDist == 0)
                return;

        if (!horizontal && !vertical) {
            horizontal = true;
            vertical = true;
        }

        if (vertical) {
            move(Direction.Up);
            if (askForMDistToUnknown() > mDist) {
                move(Direction.Down);
                move(Direction.Down);
                if (askForMDistToUnknown() > mDist) {
                    move(Direction.Up);
                    vertical = false;
                } else if (askForMDistToUnknown() == mDist)
                    vertical = false;
            } else if (askForMDistToUnknown() == mDist) {
                move(Direction.Down);
                if (askForMDistToUnknown() > mDist) {
                    move(Direction.Up);
                    vertical = false;
                }
            }
        }

        mDist = askForMDistToUnknown();

        if (horizontal) {
            move(Direction.Right);
            if (askForMDistToUnknown() > mDist) {
                move(Direction.Left);
                move(Direction.Left);
                if (askForMDistToUnknown() > mDist) {
                    move(Direction.Right);
                    horizontal = false;
                } else if (askForMDistToUnknown() == mDist)
                    horizontal = false;
            } else if (askForMDistToUnknown() == mDist) {
                move(Direction.Left);
                if (askForMDistToUnknown() > mDist) {
                    move(Direction.Right);
                    horizontal = false;
                }
            }
        }
    }

    private static void sendMessage(String jsonObject) throws Exception {
        String msg = jsonObject;


    }

    public static void main(String[] args) throws Exception {
        try {
            PlayerCommServer communicationServer = new PlayerCommServer();
            communicationServer.connect();
            Thread.sleep(5000);
            String msg = communicationServer.sendMessage("This is a message from external class");
            System.out.println("==================================="+ msg + "========================================");
            Thread.sleep(5000);
            communicationServer.closeConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
