package tictactoe;

import java.util.Objects;

public class ComputerPlayer extends Player {
    private final MoveGenerator moveGenerator;

    public ComputerPlayer(Side side, MoveGenerator moveGenerator) {
        super(side);

        this.moveGenerator = Objects.requireNonNull(moveGenerator);
    }

    public int getMove(Board board) {
        return moveGenerator.getMove(board, getSide());
    }
}
