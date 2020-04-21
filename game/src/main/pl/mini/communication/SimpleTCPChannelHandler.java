package pl.mini.communication;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        log.info(ctx.channel().remoteAddress() + " Channel Active");
        ctx.writeAndFlush("Wilkomen");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        log.info(ctx.channel().remoteAddress() + s);
        channels.writeAndFlush("Some bruh just send a message\n");


    }

}