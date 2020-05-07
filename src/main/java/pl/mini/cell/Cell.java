package pl.mini.cell;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@EqualsAndHashCode
public class Cell {
    @Getter
    @Setter
    public CellState cellState;
    public int distance = 0;
    public String playerGuid;

    public Cell(CellState cellState) {
        this.cellState = cellState;
        this.playerGuid = "";
    }

    public Cell(CellState cellState, String playerGuids, int distance) {
        this.cellState = cellState;
        this.playerGuids = playerGuids;
        this.distance = distance;
    }


    public  JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cellState", cellState.name());
        jsonObject.put("distance", distance);
        jsonObject.put("playerGuid", playerGuid);

        return jsonObject;
    }


}
