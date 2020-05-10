package pl.mini.gamemaster;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import pl.mini.position.Position;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class GameMasterClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "997"));
    static final EventLoopGroup group = new NioEventLoopGroup();
    static Channel channel;


    public void start(int port) throws InterruptedException {

        GameMaster gameMaster = new GameMaster();
        gameMaster.loadConfigurationFromJson("src/main/resources/config.json");
        for (Position p : gameMaster.getConfiguration().predefinedGoalPositions) {
            gameMaster.getBoard().setGoal(p);
        }
        final GameMasterClientHandler handler = new GameMasterClientHandler(gameMaster);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LineBasedFrameDecoder(1024));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(handler);
                        }
                    });

            channel = b.connect(HOST, port).sync().channel();


            // wait for all players to connect
            synchronized (handler) {
                handler.wait();
            }
            //start game after that
            log.info("All players connected, game is starting.");
            handler.startGame();
            // start placing pieces
            Timer timer = new Timer();
            TimerTask placePieceTask = new TimerTask() {
                @Override
                public void run() {
                    if (gameMaster.getBoard().getPiecesPosition().size() < gameMaster.getConfiguration().maxPieces) {
                        System.out.println("placing piece");
                        gameMaster.putNewPiece();
                    }


                }
            };

            timer.scheduleAtFixedRate(placePieceTask, 0, gameMaster.getConfiguration().delayNextPiecePlace);

            log.debug("Piece placing started.");


            //f.channel().closeFuture().sync();
        } finally {
            //group.shutdownGracefully();
        }
    }

    private static final class Lock {
    }


    public void closeConnection() {
        group.shutdownGracefully();
    }

    /**
     * Methods used in integration testing
     */
    public void evaluate (String msg){
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void openConnection(int port) throws InterruptedException {
        start(port);
    }

}
