package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomMoveGeneratorTest {
    @Test
    public void shouldThrowNullPointerExceptionWhenBoardIsNull() {
        assertThrows(NullPointerException.class, () -> new RandomMoveGenerator().getMove(null, Side.X));
    }

    @Test
    public void shouldGetMoveOnEmptyBoard() {
        var board = new Board();
        var move = new RandomMoveGenerator().getMove(board, Side.X);

        assertTrue(board.getAvailableMoves().contains(move));
    }

    @Test
    public void shouldGetMoveWhenBoardIsNotEmpty() {
        var board = new Board();
        board.makeMove(0, Side.X);

        var move = new RandomMoveGenerator().getMove(board, Side.O);

        assertTrue(board.getAvailableMoves().contains(move));
    }

    @Test
    public void shouldReturnDisplayName() {
        assertEquals("Random", new RandomMoveGenerator().toString());
    }
}
