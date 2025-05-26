package tictactoe;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class RandomMoveGenerator implements MoveGenerator {
    public int getMove(Board board, Side side) {
        Objects.requireNonNull(board);

        var availableMoves = board.getAvailableMoves();

        return availableMoves.get(ThreadLocalRandom.current().nextInt(availableMoves.size()));
    }

    @Override
    public String toString() {
        return "Random";
    }
}
