package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuleBasedMoveGeneratorTests {
    @Test
    public void shouldThrowNullPointerExceptionWhenBoardIsNull() {
        assertThrows(NullPointerException.class, () -> new RuleBasedMoveGenerator().getMove(null, Side.X));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSideIsNull() {
        assertThrows(NullPointerException.class, () -> new RuleBasedMoveGenerator().getMove(new Board(), null));
    }

    @Test
    public void shouldGetWinningMoveIfAvailable() {
        var board = new Board();
        board.makeMove(0, Side.X);
        board.makeMove(4, Side.O);
        board.makeMove(8, Side.X);
        board.makeMove(1, Side.O);
        board.makeMove(6, Side.X);

        var move = new RuleBasedMoveGenerator().getMove(board, Side.O);

        assertEquals(7, move);
    }

    @Test
    public void shouldBlockOpponentsWinningMove() {
        var board = new Board();
        board.makeMove(0, Side.X);
        board.makeMove(4, Side.O);
        board.makeMove(2, Side.X);

        var move = new RuleBasedMoveGenerator().getMove(board, Side.O);

        assertEquals(1, move);
    }

    @Test
    public void shouldGetCenterMoveIfAvailable() {
        var board = new Board();
        var move = new RuleBasedMoveGenerator().getMove(board, Side.X);

        assertEquals(4, move);
    }

    @Test
    public void shouldGetCornerMoveIfAvailable() {
        var board = new Board();
        board.makeMove(0, Side.X);
        board.makeMove(4, Side.O);
        board.makeMove(8, Side.X);

        var move = new RuleBasedMoveGenerator().getMove(board, Side.O);

        assertEquals(2, move);
    }

    @Test
    public void shouldGetEdgeMove() {
        var board = new Board();
        board.makeMove(4, Side.X);
        board.makeMove(0, Side.O);
        board.makeMove(2, Side.X);
        board.makeMove(6, Side.O);
        board.makeMove(3, Side.X);
        board.makeMove(5, Side.O);
        board.makeMove(8, Side.X);
        board.makeMove(1, Side.O);

        var move = new RuleBasedMoveGenerator().getMove(board, Side.X);

        assertEquals(7, move);
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenMoveIsNotAvailable() {
        var board = new Board();
        board.makeMove(4, Side.X);
        board.makeMove(0, Side.O);
        board.makeMove(2, Side.X);
        board.makeMove(6, Side.O);
        board.makeMove(3, Side.X);
        board.makeMove(5, Side.O);
        board.makeMove(8, Side.X);
        board.makeMove(1, Side.O);
        board.makeMove(7, Side.X);

        assertThrows(RuntimeException.class, () -> new RuleBasedMoveGenerator().getMove(board, Side.O));
    }

    @Test
    public void shouldReturnDisplayName() {
        assertEquals("Rule-based", new RuleBasedMoveGenerator().toString());
    }
}
