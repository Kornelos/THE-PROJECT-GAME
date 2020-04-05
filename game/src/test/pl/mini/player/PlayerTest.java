package pl.mini.player;

import org.junit.Before;
import pl.mini.board.Board;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

public class PlayerTest {

    Player player;

    @Before
    public void init() {

        player = new Player("test", new Board(2, 2, 2), new Team("testTeam", TeamColor.Red));
    }

}
