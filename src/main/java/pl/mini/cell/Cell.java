package pl.mini.cell;


import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Cell {
    @Getter
    @Setter
    public CellState cellState;
    public int distance = 0;
    public String playerGuids;

    public Cell(CellState cellState) {
        this.cellState = cellState;
        this.playerGuids = "";
    }

    public String toJsonString() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("cellState", cellState.name());
        jsonMap.put("distance", String.valueOf(distance));
        jsonMap.put("playerGuid", playerGuids);

        JSONObject json = new JSONObject(jsonMap);
        return json.toString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
