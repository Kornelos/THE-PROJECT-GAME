package pl.mini.team;

import lombok.Getter;
import lombok.Setter;
import pl.mini.player.Player;

import java.util.ArrayList;
import java.util.List;


public class Team {

    @Getter
    private final String teamName;
    //NOTE:  field below is not in class diagram
    @Getter private final List<Player> teamMembers = new ArrayList<>();
    @Getter
    @Setter
    private TeamColor color;
    private int size;

    public Team(String teamName, TeamColor color) {
        this.teamName = teamName;
        this.color = color;

    }

    public void addTeamMember(Player player) {
        teamMembers.add(player);
        player.setTeam(this);
    }

    public Player getLeader() {
        return teamMembers.stream().filter(player -> TeamRole.Leader.equals(player.getPlayerTeamRole()))
                .findAny()
                .orElse(null);
    }

}

