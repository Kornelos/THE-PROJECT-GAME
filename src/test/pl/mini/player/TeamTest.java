package pl.mini.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.mini.board.Board;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;

public class TeamTest {
    Team team = new Team("teamTest", TeamColor.Red);
    Player leader = new Player("testLeader", new Board(5, 5, 5), new Team("a", TeamColor.Red));

    @Before
    public void initTest() {
        team.addTeamMember(new Player("testPlayer1", new Board(5, 5, 5), new Team("a", TeamColor.Red)));
        team.addTeamMember(new Player("testPlayer2", new Board(5, 5, 5), new Team("a", TeamColor.Red)));
    }

    @Test
    public void testGetLeader() {
        // no leader at beginning
        Assert.assertNull(team.getLeader());
        // set leader
        leader.setPlayerTeamRole(TeamRole.Leader);
        team.addTeamMember(leader);
        // get leader
        Assert.assertEquals(leader, team.getLeader());
    }


}