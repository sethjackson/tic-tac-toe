package tictactoe;

public enum Side {
    X,
    O;

    public Side flip() {
        if (this == Side.X) {
            return Side.O;
        }

        return Side.X;
    }
}
