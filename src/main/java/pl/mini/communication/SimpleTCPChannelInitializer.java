package pl.mini.communication;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
public class SimpleTCPChannelInitializer extends ChannelInitializer<SocketChannel> {
    // static resources - they are shared between all connections
    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();

    private static final SimpleTCPChannelHandler SERVER_HANDLER = new SimpleTCPChannelHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // the encoder and decoder are static as these are sharable
        pipeline.addLast("frameDecoder", new LineBasedFrameDecoder(1024));
        pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));

        // and then business logic.
        pipeline.addLast(SERVER_HANDLER);
    }
}
