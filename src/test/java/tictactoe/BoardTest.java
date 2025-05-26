package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {
    @Test
    public void shouldSetupBoard() {
        var board = new Board();

        assertEquals(9, board.getAvailableMoves().size());
        assertNull(board.getWinner());
        assertTrue(board.isEmpty());
        assertFalse(board.isFull());
        assertFalse(board.hasWinner());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenMakingNullMove() {
        assertThrows(NullPointerException.class, () -> new Board().makeMove(0, null));
    }

    @Test
    public void shouldMakeMove() {
        var board = new Board();

        board.makeMove(0, Side.X);

        assertEquals(Side.X, board.getPieces()[0]);
    }

    @Test
    public void shouldUndoMove() {
        var board = new Board();

        board.makeMove(0, Side.X);
        board.undoMove(0);

        assertNull(board.getPieces()[0]);
    }

    @Test
    public void shouldHaveWinner() {
        var board = new Board();

        board.makeMove(0, Side.X);
        board.makeMove(1, Side.O);
        board.makeMove(4, Side.X);
        board.makeMove(2, Side.O);
        board.makeMove(8, Side.X);

        assertEquals(Side.X, board.getWinner());
        assertTrue(board.hasWinner());
    }
}
