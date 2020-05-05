package pl.mini.communication;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.mini.messages.GmConnectMessage;

@Slf4j
public class GameMasterClientHandler extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;
    @Getter
    @Setter
    private String message = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        log.info("Client active");
        ctx.writeAndFlush(new GmConnectMessage().toJsonString());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info(msg.toString());
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
