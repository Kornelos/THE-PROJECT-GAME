package pl.mini.communication;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import pl.mini.messages.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Business logic of the communication server
 **/
@Slf4j
@ChannelHandler.Sharable
public class SimpleTCPChannelHandler extends SimpleChannelInboundHandler<String> {
    // this holds reference to all active channels, also game master connection should be somehow separated.
    ChannelGroup allChannels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Map<UUID, Channel> playerChannels = new HashMap<>();
    Channel gmChannel;
    final int numOfBytes = 1024;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //ctx.channel().config().setOption( ChannelOption.SO_RCVBUF, numOfBytes );
        allChannels.add(ctx.channel());
        log.info(ctx.channel().remoteAddress() + " Channel Active");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {

        try {
            JsonMessage jsonMessage = MessageFactory.messageFromString(s);
            switch (jsonMessage.getTarget()) {
                case "server":
                    // add players and game master to prop. structures
                    if (jsonMessage.getAction() == MessageAction.connect && gmChannel != null) {

                        gmChannel.writeAndFlush(s + "\n");

                    } else if (jsonMessage.getAction() == MessageAction.gmConnect) {
                        gmChannel = ctx.channel();
                    }
                    break;
                case "all":
                    // send message to all players and game master
                    for (Channel c : allChannels) {
                        c.writeAndFlush(s + "\n");
                    }
                    break;
                case "gm":
                    // send message to gm
                    if (jsonMessage instanceof ConnectMessage) {
                        ConnectMessage playerConnect = (ConnectMessage) jsonMessage;
                        playerChannels.put(playerConnect.getPlayerGuid(), ctx.channel());
                    }

                    gmChannel.writeAndFlush(s + "\n");
                    break;
                default:
                    UUID targetUUID = UUID.fromString(jsonMessage.getTarget());
                    playerChannels.get(targetUUID).writeAndFlush(s + "\n");
                    break;
            }

        } catch (Exception e) {
            log.error("Server got corrupted message! " + s + " Exception: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn(ctx.channel().remoteAddress() + " Inactive");

        if (ctx.channel().equals(gmChannel)) {
            // if GM disconnected - disconnect all players
            log.warn("GameMaster has disconnected - server stops");
            EndMessage endMessage = new EndMessage("draw");
            for (Channel c : allChannels) {
                c.writeAndFlush(endMessage.toString()).addListener(ChannelFutureListener.CLOSE);
            }
            // remove all connections
            cleanConnections();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();
        ctx.close();
    }

    private void cleanConnections() {
        allChannels.clear();
        gmChannel = null;
        playerChannels.clear();
    }

}