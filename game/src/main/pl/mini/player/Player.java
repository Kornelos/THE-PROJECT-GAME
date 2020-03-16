package pl.mini.player;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Point2D;
import java.util.UUID;

public class Player {
    @Getter
    private final String playerName;
    // private Board board;
    private final UUID playerUuid;
    @Getter
    @Setter
    private Team team;
    // TODO: what is action type?
    // private ActionType lastAction
    private boolean piece;
    private Point2D position;
    // private PlayerState playerState;
    @Getter
    @Setter
    private TeamRole teamRole;


    public Player(String playerName) {
        this.playerName = playerName;
        piece = false;
        //TODO: player position
        playerUuid = UUID.randomUUID();
        teamRole = TeamRole.Member;
    }

    public void makeAction() {
    }

    public void discover() {
    }

    public void move() {
    }

    public void takePiece() {
        piece = true;
    }

    public void testPiece() {
    }

    public void placePiece() {
    }


}
