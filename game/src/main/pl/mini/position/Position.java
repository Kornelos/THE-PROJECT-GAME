package pl.mini.position;

import lombok.Getter;

public class Position {
    @Getter
    private int x;
    @Getter
    private int y;

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
