package pl.mini;

import lombok.SneakyThrows;
import pl.mini.board.Board;
import pl.mini.board.GameMasterBoard;
import pl.mini.gamemaster.GameMaster;
import pl.mini.gamemaster.GameMasterConfiguration;
import pl.mini.player.Player;
import pl.mini.team.Team;
import pl.mini.team.TeamColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Hello world!
 */
public class App {
    @SneakyThrows
    public static void main(String[] args) {
        // temporary game init
        System.out.println("Game is initializing..");
        GameMaster gm = new GameMaster();
        gm.setBoard(new GameMasterBoard(8, 2, 2));
        gm.setConfiguration(new GameMasterConfiguration(
                0.1, 4, 4, null, 8, 2, 2, 1, 1, 1, 1, 1, 1, 1));
        CommServerMockSingleton.INSTANCE.registerGameMaster(gm);

        // teams
        Team red = new Team("RedTeam", TeamColor.Red);
        Team blue = new Team("BlueTeam", TeamColor.Blue);
        List<UUID> red_ids = new ArrayList<>();
        List<UUID> blue_ids = new ArrayList<>();

        // players
        Player red_player = new Player("p1", new Board(8, 2, 2), red);
        red_player.setPosition(gm.getBoard().placePlayer(red_player));
        red.addTeamMember(red_player);

        Player blue_player = new Player("p2", new Board(8, 2, 5), blue);
        blue_player.setPosition(gm.getBoard().placePlayer(blue_player));
        blue.addTeamMember(blue_player);

        for(int i = 0;i<red.getTeamMembers().size();i++)
            red_ids.add(red.getTeamMembers().get(i).getPlayerUuid());
        for(int i = 0;i<blue.getTeamMembers().size();i++)
            blue_ids.add(blue.getTeamMembers().get(i).getPlayerUuid());

        gm.setTeamRedGuids(red_ids);
        gm.setTeamBlueGuids(blue_ids);

        // game loop
        System.out.println("Press ENTER to start...");
        System.in.read();
        System.out.println("Game loop starting...");
//        gm.printBoard();
        int i = 0;
        while (i < 120) {
            if (i % 5 == 0) {
                gm.putNewPiece();
            }

            gm.printBoard();
            red_player.makeAction();
//            blue_player.makeAction();
            Thread.sleep(1000);
//            gm.printBoard();
            i += 1;
        }
    }
}
