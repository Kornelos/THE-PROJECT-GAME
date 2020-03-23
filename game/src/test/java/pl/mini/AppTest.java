package java.pl.mini;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AppTest 
{
    @Test
    public void testLoadFromJSON(){
        GameMaster gm = new GameMaster();
        GameMasterConfiguration gc1 = new GameMasterConfiguration(0.8,6,7,{Point(3,4), Point(5,6), Point(7,8)},8,
                                                              6, 2, 2, 1, 1, 2, 3, 5, 2);
        gm.configuration = gm.loadConfigurationFromJson("TestJSONs/test1.json");
        Assert.assertEquals(gm.configuration, gc1);
        GameMaster gm2 = new GameMaster();
        GameMasterConfiguration gc2 = new GameMasterConfiguration(0.4,4,8,{Point(1,2), Point(3,4), Point(5,6)},10,
                                                                  7, 3, 3, 2, 2, 3, 4, 2, 3);
        gm2.configuration = gm2.loadConfigurationFromJson("TestJSONs/test2.json");
        Assert.assertEquals(gm2.configuration, gc2);
    }

    @Test
    public void testSaveToJSON(){
        GameMaster gm = new GameMaster();
        GameMasterConfiguration gc1 = new GameMasterConfiguration(0.8,6,7,{Point(3,4), Point(5,6), Point(7,8)},8,
                                                                  6, 2, 2, 1, 1, 2, 3, 5, 2);
        gm.configuration = gc1;
        gm.saveConfigurationToJson("TestJSONs/test1b.json");
        Assert.assertTrue(FileUtils.contentEquals("TestJSONs/test1.json", "TestJSONs/test1b.json"));
        GameMaster gm2 = new GameMaster();
        GameMasterConfiguration gc2 = new GameMasterConfiguration(0.4,4,8,{Point(1,2), Point(3,4), Point(5,6)},10,
                                                                  7, 3, 3, 2, 2, 3, 4, 2, 3);
        gm2.configuration = gc2;
        gm2.saveConfigurationToJson("TestJSONs/test2b.json");
        Assert.assertTrue(FileUtils.contentEquals("TestJSONs/test2.json", "TestJSONs/test2b.json"));
    }
}
