package pl.mini.messages;

import org.junit.Assert;
import org.junit.Test;
import pl.mini.board.Board;
import pl.mini.cell.Cell;
import pl.mini.cell.CellState;
import pl.mini.cell.Field;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageFactoryTest {

    @Test
    public void testFactory() throws Exception {
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

        //test start message
        List<UUID> lst = new ArrayList<>();
        lst.add(UUID.randomUUID());
        lst.add(UUID.randomUUID());
        StartMessage startMessage = new StartMessage(TeamColor.Red, TeamRole.Member,
                3, lst, new Position(6,9), new Board(9,8,7));
        JsonMessage jsonMessageStart = MessageFactory.messageFromString(startMessage.toString());
        assert jsonMessageStart != null;
        Assert.assertEquals(startMessage.getClass(), jsonMessageStart.getClass());
        StartMessage sm = (StartMessage) jsonMessageStart;
        Assert.assertEquals(startMessage, sm);

        //test end message
        EndMessage endMessage = new EndMessage("RED");
        JsonMessage jsonMessageEnd = MessageFactory.messageFromString(endMessage.toString());
        assert jsonMessageEnd != null;
        Assert.assertEquals(endMessage.getClass(), jsonMessageEnd.getClass());
        EndMessage em = (EndMessage) jsonMessageEnd;
        Assert.assertEquals(endMessage, em);

        //test pickup message
        PickupMessage pickupMessage = new PickupMessage(UUID.randomUUID());
        JsonMessage jsonMessagePickup = MessageFactory.messageFromString(pickupMessage.toString());
        assert jsonMessagePickup != null;
        Assert.assertEquals(pickupMessage.getClass(), jsonMessagePickup.getClass());
        PickupMessage pm1 = (PickupMessage) jsonMessagePickup;
        Assert.assertEquals(pickupMessage, pm1);

        //test pickup result message
        PickupResultMessage pickupResultMessage = new PickupResultMessage(UUID.randomUUID(), "DENIED");
        JsonMessage jsonMessagePickupResult = MessageFactory.messageFromString(pickupResultMessage.toString());
        assert jsonMessagePickupResult != null;
        Assert.assertEquals(pickupResultMessage.getClass(), jsonMessagePickupResult.getClass());
        PickupResultMessage prm = (PickupResultMessage) jsonMessagePickupResult;
        Assert.assertEquals(pickupResultMessage, prm);

        //test discover result message
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(new Position(3,4), new Cell(CellState.Goal)));
        fields.add(new Field(new Position(5,6), new Cell(CellState.Piece)));
        fields.add(new Field(new Position(7,8), new Cell(CellState.Empty)));
        DiscoverResultMessage discoverResultMessage = new DiscoverResultMessage(UUID.randomUUID(),
                new Position(1,2), fields);
        JsonMessage jsonMessageDiscoverResult = MessageFactory.messageFromString(discoverResultMessage.toString());
        assert jsonMessageDiscoverResult != null;
        Assert.assertEquals(discoverResultMessage.getClass(), jsonMessageDiscoverResult.getClass());
        DiscoverResultMessage drm = (DiscoverResultMessage) jsonMessageDiscoverResult;
        Assert.assertEquals(discoverResultMessage, drm);
    }
}