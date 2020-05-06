package pl.mini.communication;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import pl.mini.messages.JsonMessage;
import pl.mini.messages.MessageFactory;

/**
 * Business logic of the communication server
 **/
@Slf4j
@ChannelHandler.Sharable
public class SimpleTCPChannelHandler extends SimpleChannelInboundHandler<String> {
    // this holds reference to all active channels, also game master connection should be somehow separated.
    ChannelGroup channels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    Channel gmChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        log.info(ctx.channel().remoteAddress() + " Channel Active");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {

//        if (s.equals("GameMaster")) {
//            gmChannel = ctx.channel();
//            channels.writeAndFlush("GameMaster connected to the server.\n");
//        } else if (s.equals("Player")) {
//            channels.writeAndFlush("Player connected to the server.\n");
//        }
//        log.info(ctx.channel().remoteAddress() + s);
        try {
            JsonMessage jsonMessage = MessageFactory.messageFromString(s);
            switch (jsonMessage.getTarget()) {
                case "server":
                    //handle
                    break;
                case "all":
                    // send message to all players and game master
                    for (Channel c : channels) {
                        c.writeAndFlush(s);
                    }
                    break;
                case "gm":
                    // send message to gm
                    break;
                default:
                    // if none of the above - assume target is player uuid
                    break;
            }

        } catch (Exception e) {
            log.error("Server got corrupted message! Exception: " + e.getMessage());
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn(ctx.channel().remoteAddress() + " Inactive");

        if (ctx.channel().equals(gmChannel)) {
            // if GM disconnected - disconnect all players
            log.warn("GameMaster has disconnected - server stops");
            for (Channel c : channels) {
                c.writeAndFlush("Game Over - GM disconnected\n").addListener(ChannelFutureListener.CLOSE);
            }
            channels.clear();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();
        ctx.close();
    }


}