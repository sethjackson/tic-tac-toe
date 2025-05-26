package tictactoe;

import java.util.EventListener;

public interface GameStateChangedListener extends EventListener {
    void onGameStateChanged();
}
