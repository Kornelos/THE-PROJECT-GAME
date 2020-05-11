package pl.mini.server;

import org.junit.*;
import pl.mini.communication.*;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.mini.gamemaster.GameMasterClient;
import pl.mini.player.*;

public class GameMasterClientIntegrateTest{

    private static Thread trd;

    /**
     * Sets up the thread for integration test to be executed on
     * @throws InterruptedException
     */
    public void setUp() throws InterruptedException{
        trd = new Thread(()->{
            ServerRunner.main(new String[]{"server"});
            try {
                this.GMCSequential();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        trd.start();
        // Giving thread some time to start
        Thread.sleep(10000);
    }

    /**
     * Calls tryPlayer and catches Interrupted Exception
     */
    public void GMCSequential() throws InterruptedException {
        try {
            tryGMC();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            this.kill();
        }
    }

    /**
     * Estabilishes a connection and parses stream for server to evaluate
     * @throws InterruptedException
     */
    public void tryGMC() throws InterruptedException {
        GameMasterClient gmc = new GameMasterClient();
        gmc.openConnection(2137);
        String examp = IntStream.range(0, 1024).mapToObj(String::valueOf).collect(Collectors.joining());
        gmc.evaluate(UUID.randomUUID().toString() + ":" + examp);
        gmc.closeConnection();
    }

    /**
     * Kills thread and gives it some time to do so
     * @throws InterruptedException
     */
    public void kill() throws InterruptedException {
        Thread.sleep(1000);
        trd.interrupt();
        // waiting for thread to interrupt
        while(trd.isAlive()){
            Thread.sleep(200);
        }
    }
}