package pl.mini.player;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class Team {

    @Getter
    private final String teamName;
    private final List<Player> teamMembers = new ArrayList<>();
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
        return teamMembers.stream().filter(player -> TeamRole.Leader.equals(player.getTeamRole()))
                .findAny()
                .orElse(null);
    }

}

