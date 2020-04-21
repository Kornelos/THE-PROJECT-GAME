package pl.mini.player;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class PlayerCommServer {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "2137"));
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;
    SimplePlayerCommHandler handler;

    void connect() throws InterruptedException {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new SimplePlayerCommHandler());
                        }
                    });
            channel = b.connect(HOST, PORT).sync().channel();
            handler = (SimplePlayerCommHandler) channel.pipeline().last();

            // Wait until the connection is closed.
            //f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            //group.shutdownGracefully();
        }
    }

    public String sendMessage(String msg) throws InterruptedException {
        String return_msg;
        System.out.println("-------------------------------Sending message------------------------------------------");
        channel.writeAndFlush(msg);
        while((return_msg = handler.getMessage()) == (null));
        if(return_msg.equals("Game Over"))
            this.closeConnection();
        handler.setMessage(null);
        return return_msg;
    }

    public void closeConnection() {
        group.shutdownGracefully();
    }
}
