package tictactoe;

import java.util.Objects;

public abstract class Player {
    private final Side side;

    protected Player(Side side) {
        this.side = Objects.requireNonNull(side);
    }

    public Side getSide() {
        return side;
    }
}
