package pl.mini.server;

import org.junit.*;
import pl.mini.communication.*;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.mini.player.*;

public class PlayerCommServerIntegrateTest{

    @Test
    public void playerSequential() {
        try {
            tryPlayer();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void tryPlayer() throws InterruptedException {
        PlayerCommServer serv = new PlayerCommServer();
        serv.openConnection();
        String examp = IntStream.range(0, 1024).mapToObj(String::valueOf).collect(Collectors.joining());
        serv.evaluate(UUID.randomUUID().toString() + ":" + examp);
        serv.closeConnection();
    }
}

