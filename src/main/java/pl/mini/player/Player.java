package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.mini.board.Board;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.messages.*;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

import java.net.InetAddress;
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
            String msg = commServer.sendMessage((new ConnectMessage(playerUuid)).toString() + "\n");
            JsonMessage jmsg = MessageFactory.messageFromString(msg);
            if (jmsg.getClass() == StartMessage.class)
            {
                this.board = ((StartMessage) jmsg).getBoard();
                this.team.setColor(((StartMessage) jmsg).getTeamColor());
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
        String msg;

        if (piece)
        {
            if(board.getCellsGrid()[position.getX()][position.getY()].cellState == CellState.Unknown) {
                msg = commServer.sendMessage(new PlaceMessage(playerUuid).toString() + "\n");
                PlaceResultMessage prm = (PlaceResultMessage) MessageFactory.messageFromString(msg);
                if(prm.getStatus().toString().equals("OK"))
                {
                    if(prm.getResult().toString().equals("Correct"))
                        board.getCellsGrid()[position.getX()][position.getY()].cellState = CellState.Goal;
                    else
                        board.getCellsGrid()[position.getX()][position.getY()].cellState = CellState.Empty;
                    piece = false;
                }
            } else {
                Position goalPosition = seekGoal();
                while(goalPosition != null)
                {
                    move(goalPosition);
                    if(board.getCellsGrid()[position.getX()][position.getY()].getCellState() == CellState.Unknown)
                        break;
                }
            }
        }
        else
        {
            msg = this.commServer.sendMessage((new DiscoverMessage(playerUuid, position)).toString() + "\n");
            DiscoverResultMessage drm = (DiscoverResultMessage) MessageFactory.messageFromString(msg);
            List<Field> fieldList = drm.getFields();
            Field minField = new Field(new Position(0,0), new Cell(CellState.Empty, "", Integer.MAX_VALUE));
            for (Field f : fieldList) {
                this.board.getCellsGrid()[f.getPosition().getX()][f.getPosition().getY()] = f.getCell();
                if(f.getCell().distance < minField.getCell().distance)
                    minField = f;
            }

            move(minField.getPosition());

            if (board.getCellsGrid()[position.getX()][position.getY()].distance == 0 ||
                    board.getCellsGrid()[position.getX()][position.getY()].getCellState() == CellState.Piece) {
                msg = commServer.sendMessage(new TestMessage(playerUuid).toString() + "\n");
                TestResultMessage tsm = (TestResultMessage) MessageFactory.messageFromString(msg);
                if(tsm.getStatus().toString().equals("OK"))
                {
                    msg = commServer.sendMessage(new PickupMessage(playerUuid).toString() + "\n");
                    PickupResultMessage prm = (PickupResultMessage) MessageFactory.messageFromString(msg);
                    if(prm.getStatus().toString().equals("TRUE"))
                    {
                        board.getCellsGrid()[position.getX()][position.getY()].setCellState(CellState.Empty);
                        piece = true;
                    }
                }
            }
        }
        log.debug("Player " + playerName + " location:" + position.toString());
    }

    private void move(Position destination) throws Exception {
        String msg;
        int dirX = destination.getX() - this.position.getX();
        int dirY = destination.getY() - this.position.getY();

        if (dirX < 0) { left=true; right=false; }
        else if (dirX > 0) { left = false; right = true; }
        else { left = false; right = false; }

        if (dirY < 0) { down=true; up=false; }
        else if (dirY > 0) { down = false; up = true; }
        else { down = false; up = false; }

        MoveResultMessage mrm;
        if (down || up) {
            if (down)
                msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Down).toString() + "\n");
            else
                msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Up).toString() + "\n");
            mrm = (MoveResultMessage) MessageFactory.messageFromString(msg);
            if(mrm.getStatus().toString().equals("OK"))
                this.position = mrm.getPosition();
        }
        if (left || right) {
            if (left)
                msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Left).toString() + "\n");
            else
                msg = commServer.sendMessage(new MoveMessage(playerUuid, Direction.Right).toString() + "\n");
            mrm = (MoveResultMessage) MessageFactory.messageFromString(msg);
            if(mrm.getStatus().toString().equals("OK"))
                this.position = mrm.getPosition();
        }
    }

    private Position seekGoal() {
        int min = Integer.MAX_VALUE;
        int mDist;
        Position goalPosition = new Position(0,0);

        try {
            Cell[][] cll = this.board.getCellsGrid();

            if(this.team.getColor() == TeamColor.Red)
            {
                for (int i = 0; i < this.board.getBoardWidth(); i++)
                    for(int j = 0; j < this.board.getGoalAreaHeight(); j++) {
                        if (cll[i][j].cellState == CellState.Unknown) {
                            mDist = manhattanDistanceTwoPoints(position, new Position(i, j));
                            if (mDist < min) {
                                min = mDist;
                                goalPosition = new Position(i,j);
                            }
                        }
                    }
            }
            else if(this.team.getColor() == TeamColor.Blue)
            {
                for (int i = 0; i < this.board.getBoardWidth(); i++)
                    for(int j = this.board.getGoalAreaHeight() + this.board.getTaskAreaHeight();
                        j < this.board.getBoardHeight(); j++) {
                        if (cll[i][j].cellState == CellState.Unknown) {
                            mDist = manhattanDistanceTwoPoints(position, new Position(i, j));
                            if (mDist < min) {
                                min = mDist;
                                goalPosition = new Position(i,j);
                            }
                        }
                    }
            }

            if (min != Integer.MAX_VALUE)
                return goalPosition;
            else
                return null;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private int manhattanDistanceTwoPoints(Position pointA, Position pointB) {
        return Math.abs(pointA.getX( ) - pointB.getX( )) + Math.abs(pointA.getY( ) - pointB.getY( ));
    }

    public static void main(String[] args) {
        try {
            /*System.out.println("OK".equals(Status.OK.toString()));
            PlayerCommServer communicationServer = new PlayerCommServer();
            communicationServer.connect();
            Thread.sleep(5000);
            String msg = communicationServer.sendMessage(new ConnectMessage(UUID.randomUUID()).toString());
            System.out.println("===================================" + msg + "========================================");
            Thread.sleep(5000);*/


            // communicationServer.closeConnection();
            //String msg = "{\"action\":\"discoverResult\",\"position\":{\"x\":3,\"y\":1},\"playerGuid\":\"f6d0d621-3524-4e91-b05e-3036de26473d\",\"fields\":[{\"x\":2,\"y\":0,\"cell\":{\"distance\":0,\"playerGuid\":\"\",\"cellState\":\"Unknown\"}},{\"x\":3,\"y\":0,\"cell\":{\"distance\":0,\"playerGuid\":\"\",\"cellState\":\"Unknown\"}},{\"x\":4,\"y\":0,\"cell\":{\"distance\":0,\"playerGuid\":\"\",\"cellState\":\"Unknown\"}},{\"x\":2,\"y\":1,\"cell\":{\"distance\":0,\"playerGuid\":\"\",\"cellState\":\"Unknown\"}},{\"x\":3,\"y\":1,\"cell\":{\"distance\":0,\"playerGuid\":\"f6d0d621-3524-4e91-b05e-3036de26473d\",\"cellState\":\"Unknown\"}},{\"x\":4,\"y\":1,\"cell\":{\"distance\":0,\"playerGuid\":\"\",\"cellState\":\"Unknown\"}},{\"x\":2,\"y\":2,\"cell\":{\"distance\":0,\"playerGuid\":\"\",\"cellState\":\"Piece\"}},{\"x\":3,\"y\":2,\"cell\":{\"distance\":1,\"playerGuid\":\"\",\"cellState\":\"Empty\"}},{\"x\":4,\"y\":2,\"cell\":{\"distance\":2,\"playerGuid\":\"\",\"cellState\":\"Empty\"}}]}";
            //MessageFactory.messageFromString(msg);
            Player player = new Player("Player", new Board(1,1,1),new Team("Team", TeamColor.Red));
            while(true)
            {
                player.makeAction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
