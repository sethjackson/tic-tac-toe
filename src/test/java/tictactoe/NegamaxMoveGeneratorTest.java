package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NegamaxMoveGeneratorTest {
    @Test
    public void shouldThrowNullPointerExceptionWhenBoardIsNull() {
        assertThrows(NullPointerException.class, () -> new NegamaxMoveGenerator().getMove(null, Side.X));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSideIsNull() {
        assertThrows(NullPointerException.class, () -> new NegamaxMoveGenerator().getMove(new Board(), null));
    }

    @Test
    public void shouldGetMoveOnEmptyBoard() {
        var board = new Board();
        var move = new NegamaxMoveGenerator().getMove(board, Side.X);

        assertTrue(board.getAvailableMoves().contains(move));
    }

    @Test
    public void shouldGetMoveWhenBoardIsNotEmpty() {
        var board = new Board();
        board.makeMove(0, Side.X);

        var move = new NegamaxMoveGenerator().getMove(board, Side.O);

        assertTrue(board.getAvailableMoves().contains(move));
    }

    @Test
    public void shouldReturnDisplayName() {
        assertEquals("Negamax", new NegamaxMoveGenerator().toString());
    }
}
