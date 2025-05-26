package tictactoe;

import java.util.Objects;

public class NegamaxMoveGenerator implements MoveGenerator {
    public int getMove(Board board, Side side) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(side);

        var availableMoves = board.getAvailableMoves();

        int move = -1;
        var max = -Integer.MAX_VALUE;

        for (var position : availableMoves) {
            board.makeMove(position, side);

            var score = -negamax(board, availableMoves.size() - 1, side.flip());

            board.undoMove(position);

            if (score > max) {
                max = score;
                move = position;
            }
        }

        return move;
    }

    private int negamax(Board board, int depth, Side sideToMove) {
        if (depth == 0) {
            var winner = board.getWinner();

            if (winner == sideToMove) {
                return Integer.MAX_VALUE;
            } else if (winner == sideToMove.flip()) {
                return -Integer.MAX_VALUE;
            }

            return 0;
        }

        var max = -Integer.MAX_VALUE;

        for (var position : board.getAvailableMoves()) {
            board.makeMove(position, sideToMove);

            var score = -negamax(board, depth - 1, sideToMove.flip());

            board.undoMove(position);

            if (score > max) {
                max = score;
            }
        }

        return max;
    }

    @Override
    public String toString() {
        return "Negamax";
    }
}
