package pl.mini.cell;
import lombok.Getter;
import lombok.Setter;

public class Position {
    public enum Direction{
        Up,
        Down,
        Left,
        Right
    }
    @Getter @Setter private int x;
    @Getter @Setter private int y;
    public Direction direction;
    Position(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void changePosition(Direction direction){
this.direction=direction;
    }
}

