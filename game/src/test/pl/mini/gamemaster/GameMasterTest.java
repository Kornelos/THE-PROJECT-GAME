package pl.mini.gamemaster;

import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import pl.mini.gamemaster.GameMaster;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameMasterTest
{
    // TODO:
    // - Find a way to compare two JSONs
    // - Resolve problems with shamProbability values inside JSONs
    @Test
    public void testLoadFromJSON(){
        GameMaster gm = new GameMaster();
        GameMasterConfiguration gc1 = new GameMasterConfiguration(0.8,6,7, new Point[]{new Point(3, 4), new Point(5, 6), new Point(7, 8)},8,
                                                              6, 2, 2, 1, 1, 2, 3, 5, 2);
        // gm.setConfiguration(gm.loadConfigurationFromJson("./game/src/test/pl/mini/TestJSONs/test1.json"));
        //Assert.assertEquals(gm.getConfiguration(), gc1);
        GameMaster gm2 = new GameMaster();
        GameMasterConfiguration gc2 = new GameMasterConfiguration(0.4,4,8, new Point[]{new Point(1, 2), new Point(3, 4), new Point(5, 6)},10,
                                                                  7, 3, 3, 2, 2, 3, 4, 2, 3);
        //gm2.setConfiguration(gm2.loadConfigurationFromJson("./game/src/test/pl/mini/TestJSONs/test2.json"));
        //Assert.assertEquals(gm2.getConfiguration(), gc2);
    }

    @Test
    public void testSaveToJSON() throws IOException {
        GameMaster gm = new GameMaster();
        GameMasterConfiguration gc1 = new GameMasterConfiguration(0.8,6,7, new Point[]{new Point(3, 4), new Point(5, 6), new Point(7, 8)},8,
                                                                  6, 2, 2, 1, 1, 2, 3, 5, 2);
        ObjectMapper mapper = new ObjectMapper();
        gm.setConfiguration(gc1);
        gm.saveConfigurationToJson("./game/src/test/pl/mini/TestJSONs/test1b.json");
        // Assert.assertEquals(mapper.readTree((Files.readAllLines(Paths.get("./game/src/test/pl/mini/TestJSONs/test1.json"))).toString()),mapper.readTree((Files.readAllLines(Paths.get("./game/src/test/pl/mini/TestJSONs/test1b.json"))).toString()) );
        // Assert.assertSame(Files.readAllBytes(Paths.get("./game/src/test/pl/mini/TestJSONs/test1.json")), Files.readAllBytes(Paths.get("./game/src/test/pl/mini/TestJSONs/test1b.json")));
        GameMaster gm2 = new GameMaster();
        GameMasterConfiguration gc2 = new GameMasterConfiguration(0.4,4,8, new Point[]{new Point(1, 2), new Point(3, 4), new Point(5, 6)},10,
                                                                  7, 3, 3, 2, 2, 3, 4, 2, 3);
        gm2.setConfiguration(gc2);
        gm2.saveConfigurationToJson("./game/src/test/pl/mini/TestJSONs/test2b.json");
        //Assert.assertSame(Files.readAllBytes(Paths.get("./game/src/test/pl/mini/TestJSONs/test2.json")), Files.readAllBytes(Paths.get("./game/src/test/pl/mini/TestJSONs/test2b.json")));
    }
}
