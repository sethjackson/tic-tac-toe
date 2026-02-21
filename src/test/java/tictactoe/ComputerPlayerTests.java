package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComputerPlayerTests {
    @Test
    public void shouldThrowNullPointerExceptionWhenMoveGeneratorIsNull() {
        assertThrows(NullPointerException.class, () -> new ComputerPlayer(Side.X, null));
    }

    @Test
    public void shouldHaveSide() {
        assertEquals(Side.X, new ComputerPlayer(Side.X, new RandomMoveGenerator()).getSide());
        assertEquals(Side.O, new ComputerPlayer(Side.O, new RandomMoveGenerator()).getSide());
    }

    @Test
    public void shouldGetMove() {
        var board = new Board();
        var move = new ComputerPlayer(Side.X, new RandomMoveGenerator()).getMove(board);

        assertTrue(board.getAvailableMoves().contains(move));
    }
}
