package pl.mini;

import lombok.extern.slf4j.Slf4j;
import pl.mini.communication.ServerRunner;
import pl.mini.gamemaster.GameMasterClientRunner;
import pl.mini.player.Player;

/**
 * Hello world!
 */
@Slf4j
public class App {


    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log.error("Program needs input argument (gm | player | server)");
            return;
        }
        switch (args[0]) {
            case "gm":
                // run gm
                GameMasterClientRunner.main(args);
                break;
            case "player":
                // run player
                Player.main(args);
                break;
            case "server":
                //run server
                ServerRunner.main(args);
                break;
        }
    }
}
/*
    public static void main(String[] args) {

        log.info("Game is initializing..");
        // temporary game init
        GameMaster gm = new GameMaster();
        gm.loadConfigurationFromJson("./game/src/main/resources/config.json");
        final int boardWidth = gm.getConfiguration().boardWidth;
        final int goalHeight = gm.getConfiguration().boardGoalHeight;
        final int taskHeight = gm.getConfiguration().boardTaskHeight;
        gm.setBoard(new GameMasterBoard(boardWidth, goalHeight, taskHeight));
        CommServerMockSingleton.INSTANCE.registerGameMaster(gm);

        // teams
        Team red = new Team("RedTeam", TeamColor.Red);
        Team blue = new Team("BlueTeam", TeamColor.Blue);
        List<UUID> red_ids = new ArrayList<>();
        List<UUID> blue_ids = new ArrayList<>();

        // players
        Player red_player = new Player("p1", new Board(boardWidth, goalHeight, taskHeight), red);
        red_player.setPosition(gm.getBoard().placePlayer(red_player));
        red.addTeamMember(red_player);

        Player blue_player = new Player("p2", new Board(boardWidth, goalHeight, taskHeight), blue);
        blue_player.setPosition(gm.getBoard().placePlayer(blue_player));
        blue.addTeamMember(blue_player);

        for (int i = 0; i < red.getTeamMembers().size(); i++)
            red_ids.add(red.getTeamMembers().get(i).getPlayerUuid());
        for (int i = 0; i < blue.getTeamMembers().size(); i++)
            blue_ids.add(blue.getTeamMembers().get(i).getPlayerUuid());

        gm.setTeamRedGuids(red_ids);
        gm.setTeamBlueGuids(blue_ids);
        // setting goal position
        for (Position p : gm.getConfiguration().predefinedGoalPositions) {
            gm.getBoard().setGoal(p);
        }

        List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(red.getTeamMembers());
        allPlayers.addAll(blue.getTeamMembers());

        boolean check_red = false;
        boolean check_blue = false;
        // game loop
        //System.out.println("Press ENTER to start...");
        //System.in.read();
        log.info("Game loop starting");
        int i = 0;
        while (i < 180) {
            if (i % 5 == 0) {
                gm.putNewPiece();
                for (Player player : allPlayers) {
                    player.vertical = true;
                    player.horizontal = true;
                }
            }
            // check win cond
            if (check_red && check_blue) {
                log.info("\n---IT'S A TIE---\n");
                break;
            }
            if(check_red) {
                log.info("\n---RED TEAM WON---\n");
                break;
            }
            if (check_blue) {
                log.info("\n---BLUE TEAM WON---\n");
                break;
            }

            // show current state of the game
            gm.printBoard();

            // randomly decide who moves first (communication simulation)
            if (Math.random() > 0.5) {
                red_player.makeAction();
                blue_player.makeAction();
            } else {
                blue_player.makeAction();
                red_player.makeAction();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            check_red = gm.getBoard().checkWinCondition(red.getColor());
            check_blue = gm.getBoard().checkWinCondition(blue.getColor());


            i += 1;
        }
    }

 */

