package pl.mini.position;

import lombok.Getter;
import lombok.Setter;

public class Position {
    @Getter
    @Setter
    private int x;
    @Getter
    @Setter
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return ((Position) obj).getX() == getX() && ((Position) obj).getY() == getY();
        } else
            return false;
    }

    @Override
    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(x).append(y);
        return sb.toString().hashCode();
    }
}