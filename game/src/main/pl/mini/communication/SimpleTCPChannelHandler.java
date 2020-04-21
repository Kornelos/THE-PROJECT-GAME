package pl.mini.communication;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

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

        if (s.equals("GameMaster")) {
            gmChannel = ctx.channel();
            channels.writeAndFlush("GameMaster connected to the server.\n");
        } else if (s.equals("Player")) {
            channels.writeAndFlush("Player connected to the server.\n");
        }
        log.info(ctx.channel().remoteAddress() + s);

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