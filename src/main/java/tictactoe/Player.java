package tictactoe;

import java.util.Objects;

public abstract class Player {
    private final Side side;

    protected Player(Side side) {
        Objects.requireNonNull(side);

        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
