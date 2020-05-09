package pl.mini.player;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import pl.mini.messages.EndMessage;
import pl.mini.messages.MessageFactory;

@Slf4j
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
        System.out.println(msg);
        channel.writeAndFlush(msg);
        synchronized (handler) {
            handler.wait();
        }
        return_msg = handler.getMessage();
        System.out.println("Got a message :" + return_msg);
        try {
            if (MessageFactory.messageFromString(return_msg).getClass() == EndMessage.class) {
                log.debug("Game Over.   Result: " +
                        ((EndMessage) MessageFactory.messageFromString(return_msg)).getResult());
                this.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.setMessage(null);
        return return_msg;
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

    public void openConnection() throws InterruptedException {
        this.connect();
    }
}
