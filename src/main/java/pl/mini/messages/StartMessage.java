package pl.mini.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.mini.board.Board;
import pl.mini.position.Position;
import pl.mini.team.TeamColor;
import pl.mini.team.TeamRole;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
public class StartMessage implements JsonMessage {
    // this field is not in docs
    @Getter
    private final UUID playerGuid;
    @Getter
    private final MessageAction action = MessageAction.start;
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

    public StartMessage(UUID playerGuid, TeamColor teamColor, TeamRole teamRole, int teamSize,
                        List<UUID> teamGuids, Position position, Board board) {
        this.playerGuid = playerGuid;
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
        obj.put("playerGuid", playerGuid.toString());
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
        obj.put("board", brd);
        return obj.toString();
    }

    @Override
    public String getTarget() {
        return "all";
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
