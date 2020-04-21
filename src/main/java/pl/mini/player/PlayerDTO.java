package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;

import java.util.UUID;

public class PlayerDTO {
    @Getter
    protected UUID playerUuid;
    @Getter
    @Setter
    protected Position position;
    @Getter
    @Setter
    protected TeamRole playerTeamRole;
    @Getter
    @Setter
    protected TeamColor playerTeamColor;
    @Getter
    @Setter
    protected ActionType playerAction;

    public PlayerDTO() {
        playerUuid = UUID.randomUUID();
        playerTeamRole = TeamRole.Member;
    }
}
