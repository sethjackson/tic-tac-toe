package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    @Test
    public void shouldThrowNullPointerExceptionWhenFirstPlayerIsNull() {
        assertThrows(NullPointerException.class, () -> new Game(null, new HumanPlayer(Side.O)));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenSecondPlayerIsNull() {
        assertThrows(NullPointerException.class, () -> new Game(new HumanPlayer(Side.X), null));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenFirstPlayerHasInvalidSide() {
        assertThrows(IllegalArgumentException.class, () -> new Game(new HumanPlayer(Side.O), new HumanPlayer(Side.O)));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenSecondPlayerHasInvalidSide() {
        assertThrows(IllegalArgumentException.class, () -> new Game(new HumanPlayer(Side.X), new HumanPlayer(Side.X)));
    }

    @Test
    public void shouldSetupGame() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        assertNotNull(game.getBoard());
        assertEquals(game.getBoard().getPieces().length, game.getBoard().getAvailableMoves().size());
        assertEquals(x, game.getPlayerToMove());
        assertFalse(game.canUndoMove());
        assertFalse(game.canRedoMove());
        assertFalse(game.inProgress());
        assertFalse(game.isOver());
        assertNull(game.getWinner());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGameStateChangedListenerIsNull() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        assertThrows(NullPointerException.class, () -> game.addGameStateChangedListener(null));
    }

    @Test
    public void shouldMakeMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);

        assertEquals(Side.X, game.getBoard().getPieces()[0]);
        assertEquals(o, game.getPlayerToMove());
        assertTrue(game.inProgress());
    }

    @Test
    public void shouldMakeMultipleMoves() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);
        game.makeMove(4);

        assertEquals(Side.X, game.getBoard().getPieces()[0]);
        assertEquals(Side.O, game.getBoard().getPieces()[4]);
        assertEquals(x, game.getPlayerToMove());
        assertTrue(game.inProgress());
    }

    @Test
    public void shouldHaveFirstPlayerWinner() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);
        game.makeMove(1);
        game.makeMove(4);
        game.makeMove(2);
        game.makeMove(8);

        assertFalse(game.inProgress());
        assertTrue(game.isOver());
        assertEquals(x, game.getWinner());
    }

    @Test
    public void shouldHaveSecondPlayerWinner() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(1);
        game.makeMove(0);
        game.makeMove(2);
        game.makeMove(4);
        game.makeMove(3);
        game.makeMove(8);

        assertFalse(game.inProgress());
        assertTrue(game.isOver());
        assertEquals(o, game.getWinner());
    }

    @Test
    public void shouldHaveDraw() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);
        game.makeMove(4);
        game.makeMove(8);
        game.makeMove(5);
        game.makeMove(3);
        game.makeMove(6);
        game.makeMove(2);
        game.makeMove(1);
        game.makeMove(7);

        assertFalse(game.inProgress());
        assertTrue(game.isOver());
        assertNull(game.getWinner());
    }

    @Test
    public void shouldNotMakeMoveWhenGameIsOver() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);
        game.makeMove(4);
        game.makeMove(8);
        game.makeMove(5);
        game.makeMove(3);
        game.makeMove(6);
        game.makeMove(2);
        game.makeMove(1);
        game.makeMove(7);

        assertFalse(game.inProgress());
        assertTrue(game.isOver());
        assertNull(game.getWinner());

        game.makeMove(7);

        assertEquals(Side.X, game.getBoard().getPieces()[7]);
    }

    @Test
    public void shouldNotUndoMoveWhenNoUndoableMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.undoMove();
    }

    @Test
    public void canUndoMoveWhenUndoableMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);

        assertTrue(game.canUndoMove());
    }

    @Test
    public void shouldUndoFirstPlayerMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.addGameStateChangedListener(() -> {});

        game.makeMove(0);

        game.undoMove();

        assertNull(game.getBoard().getPieces()[0]);
        assertEquals(x, game.getPlayerToMove());
    }

    @Test
    public void shouldUndoSecondPlayerMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.addGameStateChangedListener(() -> {});

        game.makeMove(0);
        game.makeMove(4);

        game.undoMove();

        assertEquals(Side.X, game.getBoard().getPieces()[0]);
        assertNull(game.getBoard().getPieces()[4]);
        assertEquals(o, game.getPlayerToMove());
    }

    @Test
    public void shouldNotRedoMoveWhenNoRedoableMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.redoMove();
    }

    @Test
    public void canRedoMoveWhenRedoableMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.makeMove(0);

        game.undoMove();

        assertTrue(game.canRedoMove());
    }

    @Test
    public void shouldRedoFirstPlayerMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.addGameStateChangedListener(() -> {});

        game.makeMove(0);

        game.undoMove();

        game.redoMove();

        assertEquals(Side.X, game.getBoard().getPieces()[0]);
        assertEquals(o, game.getPlayerToMove());
    }

    @Test
    public void shouldRedoSecondPlayerMove() {
        var x = new HumanPlayer(Side.X);
        var o = new HumanPlayer(Side.O);
        var game = new Game(x, o);

        game.addGameStateChangedListener(() -> {});

        game.makeMove(0);
        game.makeMove(4);

        game.undoMove();
        game.undoMove();

        game.redoMove();
        game.redoMove();

        assertEquals(Side.X, game.getBoard().getPieces()[0]);
        assertEquals(Side.O, game.getBoard().getPieces()[4]);
        assertEquals(x, game.getPlayerToMove());
    }
}
