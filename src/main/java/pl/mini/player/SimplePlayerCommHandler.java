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
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("Message received: " + msg.toString().trim());
        message = msg.toString().trim();
        synchronized (this) {
            this.notifyAll();
        }
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

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("closing program..");
        System.exit(0);
    }
}
