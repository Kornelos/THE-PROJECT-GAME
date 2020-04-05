package pl.mini.position;

import lombok.*;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Position {
    @Getter
    @Setter
    private int x;
    @Getter
    @Setter
    private int y;
}