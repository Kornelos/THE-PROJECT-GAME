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

    @Test
    public void testLoadAndSaveToJSON() throws IOException {
        GameMaster gm = new GameMaster();
        GameMasterConfiguration gc1 = new GameMasterConfiguration(0.8,6,7, new Point[]{new Point(3, 4), new Point(5, 6), new Point(7, 8)},8,
                                                                  6, 2, 2, 1, 1, 2, 3, 5, 2);

        gm.setConfiguration(gc1);
        gm.saveConfigurationToJson("./game/src/test/pl/mini/TestJSONs/test1b.json");
        GameMaster gm2 = new GameMaster();
        gm2.setConfiguration(gm2.loadConfigurationFromJson("./game/src/test/pl/mini/TestJSONs/test1b.json"));
        Assert.assertEquals(gm.getConfiguration().print(),gm2.getConfiguration().print());
        GameMaster gm3 = new GameMaster();
        GameMasterConfiguration gc3 = new GameMasterConfiguration(0.4,4,8, new Point[]{new Point(1, 2), new Point(3, 4), new Point(5, 6)},10,
                                                                  7, 3, 3, 2, 2, 3, 4, 2, 3);
        gm3.setConfiguration(gc3);
        gm3.saveConfigurationToJson("./game/src/test/pl/mini/TestJSONs/test2b.json");
        GameMaster gm4 = new GameMaster();
        gm4.setConfiguration(gm4.loadConfigurationFromJson("./game/src/test/pl/mini/TestJSONs/test2b.json"));
        Assert.assertEquals(gm3.getConfiguration().print(),gm4.getConfiguration().print());

    }
}
