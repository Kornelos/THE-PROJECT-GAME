package pl.mini.server;

import org.junit.*;
import pl.mini.communication.*;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.mini.gamemaster.GameMasterClient;
import pl.mini.player.*;

public class GameMasterClientIntegrateTest{

    @Test
    public void GMCSequential() {
        try {
            tryGMC();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void tryGMC() throws InterruptedException {
        GameMasterClient gmc = new GameMasterClient();
        gmc.openConnection(2137);
        String examp = IntStream.range(0, 1024).mapToObj(String::valueOf).collect(Collectors.joining());
        gmc.evaluate(UUID.randomUUID().toString() + ":" + examp);
        gmc.closeConnection();
    }
}