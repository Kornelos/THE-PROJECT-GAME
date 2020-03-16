package pl.mini.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TeamTest {
    Team team = new Team("teamTest", TeamColor.Red);
    Player leader = new Player("testLeader");

    @Before
    public void initTest() {
        team.addTeamMember(new Player("testPlayer1"));
        team.addTeamMember(new Player("testPlayer2"));
    }

    @Test
    public void testGetLeader() {
        // no leader at beginning
        Assert.assertNull(team.getLeader());
        // set leader
        leader.setTeamRole(TeamRole.Leader);
        team.addTeamMember(leader);
        // get leader
        Assert.assertEquals(leader, team.getLeader());
    }
}