package pl.mini.communication;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import pl.mini.gamemaster.GameMaster;



public class GameMasterClient {
    static final private GameMaster gm = new GameMaster();
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "997"));
    //static final int PORT = gm.getPortNumber();
    //static final String HOST = gm.getIpAddress().toString();


    public static void start(int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
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
                            p.addLast(new GameMasterClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(HOST, port).sync();



            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
