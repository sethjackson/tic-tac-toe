package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Board {
    public static final int[][] WINNERS = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        {0, 3, 6},
        {1, 4, 7},
        {2, 5, 8},
        {0, 4, 8},
        {2, 4, 6},
    };

    private final Side[] pieces;

    public Board() {
        pieces = new Side[9];
    }

    public void makeMove(int position, Side side) {
        if (position < 0 || position > 8) {
            throw new IndexOutOfBoundsException("Invalid position");
        }

        pieces[position] = Objects.requireNonNull(side);
    }

    public void undoMove(int position) {
        if (position < 0 || position > 8) {
            throw new IndexOutOfBoundsException("Invalid position");
        }

        pieces[position] = null;
    }

    public Side[] getPieces() {
        return pieces;
    }

    public boolean hasPiece(int position) {
        if (position < 0 || position > 8) {
            throw new IndexOutOfBoundsException("Invalid position");
        }

        return pieces[position] != null;
    }

    public List<Integer> getAvailableMoves() {
        var availableMoves = new ArrayList<Integer>(9);

        for (var i = 0; i < pieces.length; i++) {
            if (pieces[i] != Side.X && pieces[i] != Side.O) {
                availableMoves.add(i);
            }
        }

        return availableMoves;
    }

    public Side getWinner() {
        for (var winners : WINNERS) {
            if (pieces[winners[0]] == pieces[winners[1]] && pieces[winners[1]] == pieces[winners[2]]) {
                return pieces[winners[0]];
            }
        }

        return null;
    }

    public boolean isEmpty() {
        return Arrays.stream(pieces).allMatch(Objects::isNull);
    }

    public boolean isFull() {
        return  Arrays.stream(pieces).allMatch(Objects::nonNull);
    }

    public boolean hasWinner() {
        return getWinner() != null;
    }
}
