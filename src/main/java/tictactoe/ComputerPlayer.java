package tictactoe;

import java.util.Objects;

public class ComputerPlayer extends Player {
    private final MoveGenerator moveGenerator;

    public ComputerPlayer(Side side, MoveGenerator moveGenerator) {
        super(side);

        Objects.requireNonNull(moveGenerator);

        this.moveGenerator = moveGenerator;
    }

    public int getMove(Board board) {
        return moveGenerator.getMove(board, getSide());
    }
}
