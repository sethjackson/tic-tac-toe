package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SideTests {
    @Test
    public void shouldFlipSide() {
        assertEquals(Side.O, Side.X.flip());
        assertEquals(Side.X, Side.O.flip());
    }
}
