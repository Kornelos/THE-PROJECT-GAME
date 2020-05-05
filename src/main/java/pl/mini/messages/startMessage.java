package pl.mini.messages;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.mini.board.Board;
import pl.mini.position.Direction;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class startMessage implements JsonMessage {
    @Getter
    private final MessageAction action;
    @Getter
    private final TeamColor teamColor;
    @Getter
    private final TeamRole teamRole;
    @Getter
    private final int teamSize;
    @Getter
    private final List<UUID> teamGuids;
    @Getter
    private final Position position;
    @Getter
    private final Board board;

    public startMessage(MessageAction action, TeamColor teamColor, TeamRole teamRole, int teamSize,
                        List<UUID> teamGuids, Position position, Board board) {
        this.action = action;
        this.teamColor = teamColor;
        this.teamRole = teamRole;
        this.teamSize = teamSize;
        this.teamGuids = teamGuids;
        this.position = position;
        this.board = board;
    }


    @Override
    public String toJsonString() {
        JSONObject obj = new JSONObject();
        JSONObject brd = new JSONObject();
        JSONObject pos = new JSONObject();
        JSONArray guids = new JSONArray();
        obj.put("action", action.toString());
        obj.put("teamColor", teamColor.toString());
        obj.put("teamRole", teamRole.toString());
        obj.put("teamSize", teamSize);
        for (UUID u : teamGuids)
            guids.add(u.toString());
        obj.put("teamGuids", guids);
        pos.put("x", position.getX());
        pos.put("y", position.getY());
        obj.put("position", pos);
        brd.put("boardWidth", board.getBoardWidth());
        brd.put("taskAreaHeight", board.getTaskAreaHeight());
        brd.put("goalAreaHeight", board.getGoalAreaHeight());
        obj.put("board",brd);
        return obj.toString();
    }
}
