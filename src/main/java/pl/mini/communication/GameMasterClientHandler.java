package pl.mini.communication;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.mini.board.GameMasterBoard;
import pl.mini.board.PlacementResult;
import pl.mini.cell.CellState;
import pl.mini.messages.*;
import pl.mini.player.PlayerDTO;
import pl.mini.position.Position;

@Slf4j
public class GameMasterClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    @Getter
    @Setter
    private String message = null;

    private final GameMasterBoard gameMasterBoard;

    public GameMasterClientHandler(GameMasterBoard gmb) {
        gameMasterBoard = gmb;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        log.info("Client active");
        ctx.writeAndFlush(new GmConnectMessage().toJsonString());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.debug(msg.toString());
        try {
            JsonMessage jsonMessage = MessageFactory.messageFromString(msg.toString());
            switch (jsonMessage.getAction()) {
                case discover: {
                    DiscoverMessage discoverMessage = (DiscoverMessage) jsonMessage;
                    //TODO: implement
                    break;
                }
                case pickup: {
                    PickupMessage pickupMessage = (PickupMessage) jsonMessage;
                    Position playerPosition = gameMasterBoard.findPlayerPositionByGuid(pickupMessage.getPlayerGuid().toString());
                    PickupResultMessage resultMessage;

                    if (playerPosition != null) {
                        if (gameMasterBoard.getField(playerPosition).getCell().getCellState() == CellState.Piece) {
                            gameMasterBoard.takePiece(playerPosition);
                            resultMessage = new PickupResultMessage(pickupMessage.getPlayerGuid(), Status.OK);
                        } else {
                            resultMessage = new PickupResultMessage(pickupMessage.getPlayerGuid(), Status.DENIED);
                        }
                        ctx.writeAndFlush(resultMessage.toString());

                    }
                    break;
                }
                case move: {
                    MoveMessage moveMessage = (MoveMessage) jsonMessage;
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayerUuid(moveMessage.getPlayerGuid());
                    Position playerPosition = gameMasterBoard.findPlayerPositionByGuid(playerDTO.getPlayerUuid().toString());
                    if (playerPosition != null) {
                        playerDTO.setPosition(playerPosition);
                        Position newPosition = gameMasterBoard.playerMove(playerDTO, moveMessage.getDirection());
                        // send response
                        MoveResultMessage resultMessage = new MoveResultMessage(playerDTO.getPlayerUuid(),
                                moveMessage.getDirection(), newPosition, Status.OK);
                        ctx.writeAndFlush(resultMessage.toString());
                    }

                    break;
                }
                case test:
                    //TODO: implement
                    break;

                case place: {
                    PlaceMessage placeMessage = (PlaceMessage) jsonMessage;
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setPlayerUuid(placeMessage.getPlayerGuid());
                    Position playerPosition = gameMasterBoard.findPlayerPositionByGuid(playerDTO.getPlayerUuid().toString());
                    PlaceResultMessage placeResultMessage;
                    if (playerPosition != null) {
                        playerDTO.setPosition(playerPosition);
                        //note: quick fix
                        double SHAM_PROB = 0.3;
                        PlacementResult placementResult = gameMasterBoard.placePiece(playerDTO, SHAM_PROB);
                        placeResultMessage = new PlaceResultMessage(playerDTO.getPlayerUuid(), placementResult, Status.OK);
                    }
                    break;
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
        log.info("closing program..");
        System.exit(0);
    }

}
