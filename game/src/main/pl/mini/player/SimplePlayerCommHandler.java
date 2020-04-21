package pl.mini.player;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimplePlayerCommHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    @Getter
    @Setter
    private String message = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        log.info("Connected to: " + ctx.channel().remoteAddress());
        ctx.writeAndFlush("Player connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Message received: " + msg.toString());
        if(msg.equals("Game Over")) {
            super.channelInactive(ctx);
            return;
        }
        message = msg.toString();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
