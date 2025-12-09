package tictactoe;

import java.util.Objects;

public class RuleBasedMoveGenerator implements MoveGenerator {
    private static final int CENTER = 4;
    private static final int[] CORNERS = new int[] {0, 2, 6, 8};
    private static final int[] EDGES = new int[] {1, 3, 5, 7};

    @Override
    public int getMove(Board board, Side side) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(side);

        // 1. win if possible
        var winner = getWinner(board, side);
        if (winner != null) {
            return winner;
        }

        // 2. block opponent's win
        var opponentsWinner = getWinner(board, side.flip());
        if (opponentsWinner != null) {
            return opponentsWinner;
        }

        // 3. play the center
        if (!board.hasPiece(CENTER)) {
            return CENTER;
        }

        // 4. play the corner
        for (var corner : CORNERS) {
            if (!board.hasPiece(corner)) {
                return corner;
            }
        }

        // 5. play the middle edge
        for (var edge : EDGES) {
            if (!board.hasPiece(edge)) {
                return edge;
            }
        }

        throw new RuntimeException("Unreachable");
    }

    private Integer getWinner(Board board, Side side) {
        var pieces = board.getPieces();

        for (var winners : Board.WINNERS) {
            var matchCount = 0;
            Integer emptyIndex = null;

            for (var i : winners) {
                if (pieces[i] == null) {
                    emptyIndex = i;
                } else if (pieces[i] == side) {
                    matchCount += 1;
                }
            }

            if (matchCount == 2 && emptyIndex != null) {
                return emptyIndex;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Rule-based";
    }
}
