package pl.mini.communication;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import pl.mini.gamemaster.GameMaster;


public class GameMasterClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "997"));
    static final EventLoopGroup group = new NioEventLoopGroup();
    static Channel channel;
    static GameMasterClientHandler handler;


     public static void start(int port) throws InterruptedException {
         GameMaster gameMaster = new GameMaster();
         gameMaster.loadConfigurationFromJson("src/main/resources/config.json");

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
                             p.addLast(new GameMasterClientHandler(gameMaster.getBoard()));
                        }
                    });

            channel = b.connect(HOST, port).sync().channel();
            handler = (GameMasterClientHandler) channel.pipeline().last();



            //f.channel().closeFuture().sync();
        } finally {
            //group.shutdownGracefully();
        }
    }



    public void closeConnection() {
        group.shutdownGracefully();
    }

    /**
     *  Methods used in integration testing
     */
    public void evaluate (String msg){
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void openConnection(int port) throws InterruptedException {
        start(port);
    }

}
