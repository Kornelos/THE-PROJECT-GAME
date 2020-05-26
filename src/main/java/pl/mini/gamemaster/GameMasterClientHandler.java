package pl.mini.gamemaster;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.mini.board.Board;
import pl.mini.board.PlacementResult;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.messages.*;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@ChannelHandler.Sharable
public class GameMasterClientHandler extends ChannelInboundHandlerAdapter {
    private final GameMaster gameMaster;
    @Getter
    @Setter
    private String message = null;
    private final List<PlayerDTO> players = new ArrayList<>();

    private Channel serverChannel;
    private boolean gameStarted = false;

    public GameMasterClientHandler(GameMaster gm) { gameMaster = gm; }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        serverChannel = ctx.channel();
        log.info("Client active");
        ctx.writeAndFlush(new GmConnectMessage().toJsonString() + "\n");


    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info(msg.toString());
        try {
            JsonMessage jsonMessage = MessageFactory.messageFromString(msg.toString());
            switch (jsonMessage.getAction()) {
                case connect: {
                    // add player to team and place him on board
                    ConnectMessage connectMessage = (ConnectMessage) jsonMessage;
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayerUuid(connectMessage.getPlayerGuid());
                    TeamColor teamColor = gameMaster.assignPlayerToTeam(playerDTO);
                    if (teamColor != null) {
                        playerDTO.setPlayerTeamColor(teamColor);
                        Position position = gameMaster.getBoard().placePlayer(playerDTO);
                        playerDTO.setPosition(position);
                        players.add(playerDTO);
                        log.info("player connected: " + playerDTO.getPlayerUuid().toString());
                    }
                    synchronized (this) {
                        if (players.size() == 2 * gameMaster.getConfiguration().maxTeamSize && !gameStarted) {
                            this.notifyAll();
                            gameStarted = true;
                        }

                    }
                    break;
                }
                case discover: {
                    //start
                    long lStartTime = System.currentTimeMillis();

                    DiscoverMessage discoverMessage = (DiscoverMessage) jsonMessage;
                    List<Field> fields = gameMaster.getBoard().discover(discoverMessage.getPosition());

                    DiscoverResultMessage discoverResultMessage = new DiscoverResultMessage(discoverMessage.getPlayerGuid(), discoverMessage.getPosition(), fields);
                    //end
                    long lEndTime = System.currentTimeMillis();
                    //time elapsed
                    long diff = lEndTime - lStartTime;
                    if (gameMaster.getConfiguration().delayDiscover - diff > 0)
                        Thread.sleep(gameMaster.getConfiguration().delayDiscover - diff);

                    ctx.writeAndFlush(discoverResultMessage.toString() + "\n");
                    break;
                }
                case pickup: {
                    long lStartTime = System.currentTimeMillis();
                    PickupMessage pickupMessage = (PickupMessage) jsonMessage;
                    Position playerPosition = gameMaster.getBoard().findPlayerPositionByGuid(pickupMessage.getPlayerGuid().toString());
                    PickupResultMessage resultMessage;

                    if (playerPosition != null) {
                        if (gameMaster.getBoard().getField(playerPosition).getCell().getCellState() == CellState.Piece) {
                            gameMaster.getBoard().takePiece(playerPosition);
                            resultMessage = new PickupResultMessage(pickupMessage.getPlayerGuid(), Status.OK);
                        } else {
                            resultMessage = new PickupResultMessage(pickupMessage.getPlayerGuid(), Status.DENIED);
                        }
                        long lEndTime = System.currentTimeMillis();
                        long diff = lEndTime - lStartTime;
                        if (gameMaster.getConfiguration().delayPick - diff > 0)
                            Thread.sleep(gameMaster.getConfiguration().delayPick - diff);
                        ctx.writeAndFlush(resultMessage.toString() + "\n");

                    }
                    break;
                }
                case move: {
                    long lStartTime = System.currentTimeMillis();
                    MoveMessage moveMessage = (MoveMessage) jsonMessage;
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayerUuid(moveMessage.getPlayerGuid());
                    MoveResultMessage resultMessage;
                    Position playerPosition = gameMaster.getBoard().findPlayerPositionByGuid(playerDTO.getPlayerUuid().toString());
                    if (playerPosition != null) {
                        playerDTO.setPosition(playerPosition);
                        Position newPosition = gameMaster.getBoard().playerMove(playerDTO, moveMessage.getDirection());
                        // send response
                        resultMessage = new MoveResultMessage(playerDTO.getPlayerUuid(),
                                moveMessage.getDirection(), newPosition, Status.OK);

                    } else {
                        resultMessage = new MoveResultMessage(playerDTO.getPlayerUuid(),
                                moveMessage.getDirection(), new Position(-1, -1), Status.DENIED);
                        ctx.writeAndFlush(resultMessage.toString());
                    }
                    long lEndTime = System.currentTimeMillis();
                    long diff = lEndTime - lStartTime;
                    if (gameMaster.getConfiguration().delayMove - diff > 0)
                        Thread.sleep(gameMaster.getConfiguration().delayMove - diff);

                    ctx.writeAndFlush(resultMessage.toString() + "\n");
                    gameMaster.printBoard();
                    // post to flask ui
                    // TODO: checking if flask is online
                    //gameMaster.sendBoardState();
                    break;
                }
                case test: {
                    long lStartTime = System.currentTimeMillis();
                    TestMessage testMessage = (TestMessage) jsonMessage;
                    TestResultMessage testResultMessage = new TestResultMessage(testMessage.getPlayerGuid(), Status.OK, Test.TRUE);
                    long lEndTime = System.currentTimeMillis();
                    long diff = lEndTime - lStartTime;
                    if (gameMaster.getConfiguration().delayPlace - diff > 0)
                        Thread.sleep(gameMaster.getConfiguration().delayPlace - diff);
                    ctx.writeAndFlush(testResultMessage.toString() + "\n");

                    break;
                }
                case place: {
                    long lStartTime = System.currentTimeMillis();
                    PlaceMessage placeMessage = (PlaceMessage) jsonMessage;
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayerUuid(placeMessage.getPlayerGuid());
                    Position playerPosition = gameMaster.getBoard().findPlayerPositionByGuid(playerDTO.getPlayerUuid().toString());
                    PlaceResultMessage placeResultMessage;
                    if (playerPosition != null) {
                        playerDTO.setPosition(playerPosition);
                        //note: quick fix
                        double SHAM_PROB = 0.3;
                        PlacementResult placementResult = gameMaster.getBoard().placePiece(playerDTO, SHAM_PROB);
                        placeResultMessage = new PlaceResultMessage(playerDTO.getPlayerUuid(), placementResult, Status.OK);
                        // check win condition
                        if (gameMaster.getTeamRedGuids().contains(playerDTO.getPlayerUuid())) {
                            if (gameMaster.getBoard().checkWinCondition(TeamColor.Red))
                                ctx.writeAndFlush(new EndMessage("Red won").toString() + "\n");
                        } else {
                            if (gameMaster.getBoard().checkWinCondition(TeamColor.Blue))
                                ctx.writeAndFlush(new EndMessage("Blue won").toString() + "\n");
                        }

                        long lEndTime = System.currentTimeMillis();
                        long diff = lEndTime - lStartTime;
                        if (gameMaster.getConfiguration().delayPlace - diff > 0)
                            Thread.sleep(gameMaster.getConfiguration().delayPlace - diff);

                        ctx.writeAndFlush(placeResultMessage.toString() + "\n");
                    }
                    break;
                }
                case end: {
                    log.info("Game finished");
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            log.error("GM got corrupted message! Exception: " + e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("Channel inactive, quitting");
        System.exit(0);
    }

    public void startGame() {
        // send start game message to every player
        for (var p : players) {
            StartMessage startMessage;
            List<UUID> teamList;
            if (p.getPlayerTeamColor() == TeamColor.Red) {
                teamList = gameMaster.getTeamRedGuids();
            } else {
                teamList = gameMaster.getTeamBlueGuids();
            }
            startMessage = new StartMessage(p.getPlayerUuid(), p.getPlayerTeamColor(), p.getPlayerTeamRole(),
                    gameMaster.getTeamRedGuids().size(), teamList, p.getPosition(),
                    new Board(gameMaster.getBoard().getBoardWidth(), gameMaster.getBoard().getGoalAreaHeight(), gameMaster.getBoard().getTaskAreaHeight()));
            serverChannel.writeAndFlush(startMessage.toString() + "\n");
        }
    }


}
