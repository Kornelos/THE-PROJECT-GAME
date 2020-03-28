package pl.mini.position;

import lombok.Getter;
import lombok.Setter;

public class Position {
    @Getter @Setter
    private int x;
    @Getter @Setter
    private int y;

    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void changePosition(Direction direction) {
        //TODO: out of bounds prevention
        switch (direction) {
            case Up:
                y += 1;
            case Down:
                y -= 1;
            case Left:
                x -= 1;
            case Right:
                x += 1;
        }
    }


}
