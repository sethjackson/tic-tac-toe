package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTests {
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
    public void shouldThrowIndexOutOfBoundsExceptionWhenMakingMoveWithInvalidPosition() {
        var board = new Board();

        assertThrows(IndexOutOfBoundsException.class, () -> new Board().makeMove(-1, null));
        assertThrows(IndexOutOfBoundsException.class, () -> new Board().makeMove(9, null));
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
    public void shouldThrowIndexOutOfBoundsExceptionWhenUndoingMoveWithInvalidPosition() {
        var board = new Board();

        assertThrows(IndexOutOfBoundsException.class, () -> new Board().undoMove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> new Board().undoMove(9));
    }

    @Test
    public void shouldUndoMove() {
        var board = new Board();

        board.makeMove(0, Side.X);
        board.undoMove(0);

        assertNull(board.getPieces()[0]);
    }

    @Test
    public void shouldThrowIndexOutOfBoundsExceptionWhenCheckingPieceWithInvalidPosition() {
        var board = new Board();

        assertThrows(IndexOutOfBoundsException.class, () -> board.hasPiece(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> board.hasPiece(9));
    }

    @Test
    public void shouldNotHavePieceWhenPieceNotPresent() {
        var board = new Board();

        assertFalse(board.hasPiece(0));
    }

    @Test
    public void shouldHavePieceWhenPiecePresent() {
        var board = new Board();

        board.makeMove(0, Side.X);

        assertTrue(board.hasPiece(0));
    }

    @Test
    public void shouldHaveWinnerWhenThereIsWinner() {
        var board = new Board();

        board.makeMove(0, Side.X);
        board.makeMove(1, Side.O);
        board.makeMove(4, Side.X);
        board.makeMove(2, Side.O);
        board.makeMove(8, Side.X);

        assertEquals(Side.X, board.getWinner());
        assertTrue(board.hasWinner());
    }

    @Test
    public void shouldNotHaveWinnerWhenGameIsDrawn() {
        var board = new Board();

        board.makeMove(0, Side.X);
        board.makeMove(4, Side.O);
        board.makeMove(2, Side.X);
        board.makeMove(1, Side.O);
        board.makeMove(7, Side.X);
        board.makeMove(6, Side.O);
        board.makeMove(8, Side.X);
        board.makeMove(5, Side.O);
        board.makeMove(3, Side.X);

        assertNull(board.getWinner());
        assertFalse(board.hasWinner());
        assertEquals(0, board.getAvailableMoves().size());
    }
}
