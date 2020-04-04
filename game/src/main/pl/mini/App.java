package pl.mini;

import pl.mini.board.Board;
import pl.mini.board.GameMasterBoard;
import pl.mini.gamemaster.GameMaster;
import pl.mini.player.Player;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // temporary game init
        System.out.println("Game is initializing..");
        GameMaster gm = new GameMaster();
        gm.setBoard(new GameMasterBoard(8, 2, 5));
        CommServerMockSingleton.INSTANCE.registerGameMaster(gm);

        // teams
        Team red = new Team("RedTeam", TeamColor.Red);
        Team blue = new Team("BlueTeam", TeamColor.Blue);

        // players
        Player red_player = new Player("p1", new Board(8, 2, 5), red);
        red_player.setPosition(gm.getBoard().placePlayer(red_player));

        Player blue_player = new Player("p2", new Board(8, 2, 5), blue);
        blue_player.setPosition(gm.getBoard().placePlayer(blue_player));

        // game loop
        int i = 0;
        while (i < 100) {
            red_player.makeAction();
            blue_player.makeAction();
            i += 1;
        }
    }
}
