package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Game {
    private final Player x;
    private final Player o;
    private Player playerToMove;

    private final Board board;

    private final Stack<Integer> undoStack;
    private final Stack<Integer> redoStack;

    private final List<GameStateChangedListener> gameStateChangedListeners;

    public Game(Player x, Player o) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(o);

        if (x.getSide() != Side.X) {
            throw new IllegalArgumentException("Player has invalid side.");
        }

        if (o.getSide() != Side.O) {
            throw new IllegalArgumentException("Player has invalid side.");
        }

        this.x = x;
        this.o = o;

        this.playerToMove = x;

        this.board = new Board();

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();

        this.gameStateChangedListeners = new ArrayList<>();
    }

    public void makeMove(int position) {
        if (isOver()) {
            return;
        }

        redoStack.clear();

        makeMoveInternal(position);
    }

    private void makeMoveInternal(int position) {
        board.makeMove(position, playerToMove.getSide());

        undoStack.push(position);

        if (playerToMove == x) {
            playerToMove = o;
        } else {
            playerToMove = x;
        }

        for (var listener : gameStateChangedListeners) {
            listener.onGameStateChanged();
        }
    }

    public void undoMove() {
        if (!canUndoMove()) {
            return;
        }

        var position = undoStack.pop();

        board.undoMove(position);

        redoStack.push(position);

        if (playerToMove == x) {
            playerToMove = o;
        } else {
            playerToMove = x;
        }

        for (var listener : gameStateChangedListeners) {
            listener.onGameStateChanged();
        }
    }

    public void redoMove() {
        if (!canRedoMove()) {
            return;
        }

        makeMoveInternal(redoStack.pop());
    }

    public void addGameStateChangedListener(GameStateChangedListener listener) {
        gameStateChangedListeners.add(Objects.requireNonNull(listener));
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }

    public boolean canUndoMove() {
        return !undoStack.isEmpty();
    }

    public boolean canRedoMove() {
        return !redoStack.isEmpty();
    }

    public boolean inProgress() {
        return !board.isEmpty() && !isOver();
    }

    public boolean isOver() {
        return board.hasWinner() || board.isFull();
    }

    public Player getWinner() {
        var winner = board.getWinner();

        if (winner == Side.X) {
            return x;
        } else if (winner == Side.O) {
            return o;
        }

        return null;
    }
}
