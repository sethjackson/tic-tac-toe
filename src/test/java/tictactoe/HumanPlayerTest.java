package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HumanPlayerTest {
    @Test
    public void shouldHaveSide() {
        assertEquals(Side.X, new HumanPlayer(Side.X).getSide());
        assertEquals(Side.O, new HumanPlayer(Side.O).getSide());
    }
}
