package pl.mini.player;

import lombok.Getter;
import lombok.Setter;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.Team;

import java.net.InetAddress;

public class Player extends PlayerDTO {
    @Getter
    private final String playerName;
    // private Board board;
    @Getter
    @Setter
    private Team team;
    private ActionType lastAction;
    private Direction lastDirection;
    private boolean piece;
    private Position position;
    private PlayerState playerState;
    private int portNumber;
    private InetAddress ipAddress;

    public Player(String playerName) {
        super();
        this.playerName = playerName;
        piece = false;
    }

    public void listen() {

    }

    public void makeAction() {
    }

    private void discover() {
    }

    private void move() {
    }

    private void takePiece() {
        piece = true;
    }

    private void testPiece() {
    }

    private void placePiece() {
    }


}
