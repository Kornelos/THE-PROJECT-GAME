package pl.mini.messages;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class MessageFactoryTest {

    @Test
    public void testFactory() throws ParseException {
        ConnectMessage connectMessage = new ConnectMessage(UUID.randomUUID());

        // test player connect message creation
        JsonMessage jsonMessagePlayer = MessageFactory.messageFromString(connectMessage.toString());
        assert jsonMessagePlayer != null;
        Assert.assertEquals(connectMessage.getClass(), jsonMessagePlayer.getClass());
        ConnectMessage cm = (ConnectMessage) jsonMessagePlayer;
        Assert.assertEquals(connectMessage.getPlayerGuid(), cm.getPlayerGuid());

        // test gm connect
        GmConnectMessage gmConnectMessage = new GmConnectMessage();
        JsonMessage jsonMessageGm = MessageFactory.messageFromString(gmConnectMessage.toString());
        assert jsonMessageGm != null;
        Assert.assertEquals(gmConnectMessage.getClass(), jsonMessageGm.getClass());

        //test place message creation
        PlaceMessage placeMessage= new PlaceMessage(UUID.randomUUID());
        JsonMessage jsonMessagePlace = MessageFactory.messageFromString(placeMessage.toString());
        assert jsonMessagePlace != null;
        Assert.assertEquals(placeMessage.getClass(), jsonMessagePlace.getClass());
        PlaceMessage pm = (PlaceMessage) jsonMessagePlace;
        Assert.assertEquals(placeMessage.getPlayerGuid(), pm.getPlayerGuid());

    }
}
