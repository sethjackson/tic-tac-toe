package tictactoe;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

public class MainWindow extends JFrame {
    private JMenuItem undoMoveMenuItem;
    private JMenuItem redoMoveMenuItem;

    private List<JButton> buttons;

    private Game game;

    public MainWindow() {
        setTitle("Tic-Tac-Toe");
        setPreferredSize(new Dimension(640, 480));
        setMinimumSize(getPreferredSize());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                if (canQuit()) {
                    System.exit(0);
                }
            }
        });

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
            Desktop.getDesktop().setQuitHandler((e, response) -> {
                if (canQuit()) {
                    response.performQuit();
                } else {
                    response.cancelQuit();
                }
            });
        }

        createMenus();
        createButtons();

        pack();

        setVisible(true);
    }

    private void createMenus() {
        var menuBar = new JMenuBar();

        var gameMenu = new JMenu("Game");

        var newGameMenuItem = new JMenuItem("New");
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        newGameMenuItem.addActionListener(action -> newGame());
        gameMenu.add(newGameMenuItem);

        if (!(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_QUIT_STRATEGY))) {
            var quitMenuItem = new JMenuItem("Quit");
            quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            quitMenuItem.addActionListener(action -> {
                if (canQuit()) {
                    System.exit(0);
                }
            });
            gameMenu.add(quitMenuItem);
        }

        menuBar.add(gameMenu);

        var moveMenu = new JMenu("Move");

        undoMoveMenuItem = new JMenuItem("Undo");
        undoMoveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        undoMoveMenuItem.addActionListener(action -> undoMove());
        undoMoveMenuItem.setEnabled(false);
        moveMenu.add(undoMoveMenuItem);

        redoMoveMenuItem = new JMenuItem("Redo");
        redoMoveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK));
        redoMoveMenuItem.addActionListener(action -> redoMove());
        redoMoveMenuItem.setEnabled(false);
        moveMenu.add(redoMoveMenuItem);

        menuBar.add(moveMenu);

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.APP_MENU_BAR)) {
            Desktop.getDesktop().setDefaultMenuBar(menuBar);
        } else {
            setJMenuBar(menuBar);
        }
    }

    private void createButtons() {
        buttons = new ArrayList<>();

        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3));

        for (var i = 0; i < 9; i++) {
            var button = new JButton();
            button.setFont(button.getFont().deriveFont(Font.BOLD, 40));
            button.setFocusable(false);
            button.setEnabled(false);
            button.addActionListener(this::buttonClicked);

            buttonPanel.add(button);
            buttons.add(button);
        }

        add(buttonPanel);
    }

    private void newGame() {
        if (game != null && game.inProgress()) {
            var result = JOptionPane.showConfirmDialog(
                this,
                "A game is currently in progress. Are you sure you want to start a new game?",
                "New game?",
                JOptionPane.YES_NO_OPTION
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }
        }

        var moveGeneratorTypes = List.of(
            new NegamaxMoveGenerator(),
            new RandomMoveGenerator()
        );

        var newGamePanel = new JPanel();
        newGamePanel.setLayout(new GridLayout(1, 2));

        var xPanel = new JPanel();
        xPanel.setLayout(new GridLayout(2, 2));
        xPanel.setBorder(BorderFactory.createTitledBorder("X"));

        var xButtonGroup = new ButtonGroup();

        var xHumanButton = new JRadioButton("Human");
        xHumanButton.setSelected(true);

        var xComputerButton = new JRadioButton("Computer");

        xButtonGroup.add(xHumanButton);
        xButtonGroup.add(xComputerButton);

        xPanel.add(xHumanButton);
        xPanel.add(xComputerButton);

        var xMoveGeneratorLabel = new JLabel("Move generator: ");
        xPanel.add(xMoveGeneratorLabel);

        var xMoveGenerator = new JComboBox<>(moveGeneratorTypes.toArray());
        xMoveGenerator.setEnabled(false);

        xHumanButton.addActionListener(action -> xMoveGenerator.setEnabled(xComputerButton.isSelected()));
        xComputerButton.addActionListener(action -> xMoveGenerator.setEnabled(xComputerButton.isSelected()));

        xPanel.add(xMoveGenerator);

        newGamePanel.add(xPanel);

        var oPanel = new JPanel();
        oPanel.setLayout(new GridLayout(2, 2));
        oPanel.setBorder(BorderFactory.createTitledBorder("O"));

        var oButtonGroup = new ButtonGroup();

        var oHumanButton = new JRadioButton("Human");
        oHumanButton.setSelected(true);

        var oComputerButton = new JRadioButton("Computer");

        oButtonGroup.add(oHumanButton);
        oButtonGroup.add(oComputerButton);

        oPanel.add(oHumanButton);
        oPanel.add(oComputerButton);

        var oMoveGeneratorLabel = new JLabel("Move generator: ");
        oPanel.add(oMoveGeneratorLabel);

        var oMoveGenerator = new JComboBox<>(moveGeneratorTypes.toArray());
        oMoveGenerator.setEnabled(false);

        oHumanButton.addActionListener(action -> oMoveGenerator.setEnabled(oComputerButton.isSelected()));
        oComputerButton.addActionListener(action -> oMoveGenerator.setEnabled(oComputerButton.isSelected()));

        oPanel.add(oMoveGenerator);

        newGamePanel.add(oPanel);

        var result = JOptionPane.showOptionDialog(
            this,
            newGamePanel,
            "New Game",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            null
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Player x = null;
        if (xHumanButton.isSelected()) {
            x = new HumanPlayer(Side.X);
        } else if (xComputerButton.isSelected()) {
            x = new ComputerPlayer(Side.X, (MoveGenerator) xMoveGenerator.getSelectedItem());
        }

        Player o = null;
        if (oHumanButton.isSelected()) {
            o = new HumanPlayer(Side.O);
        } else if (oComputerButton.isSelected()) {
            o = new ComputerPlayer(Side.O, (MoveGenerator) oMoveGenerator.getSelectedItem());
        }

        game = new Game(x, o);
        game.addGameStateChangedListener(this::gameStateChanged);

        gameStateChanged();
    }

    private boolean canQuit() {
        if (game == null) {
            return true;
        }

        if (game.inProgress()) {
            var result = JOptionPane.showConfirmDialog(
                this,
                "A game is currently in progress. Are you sure you want to quit?",
                "Quit game?",
                JOptionPane.YES_NO_OPTION
            );

            return result == JOptionPane.OK_OPTION;
        }

        return true;
    }

    private void undoMove() {
        if (game == null) {
            return;
        }

        game.undoMove();
    }

    private void redoMove() {
        if (game == null) {
            return;
        }

        game.redoMove();
    }

    private void buttonClicked(ActionEvent action) {
        if (game == null) {
            return;
        }

        var position = buttons.indexOf((JButton) action.getSource());

        if (game.getBoard().getPieces()[position] != null) {
            return;
        }

        game.makeMove(position);
    }

    private void gameStateChanged() {
        undoMoveMenuItem.setEnabled(game.canUndoMove());
        redoMoveMenuItem.setEnabled(game.canRedoMove());

        for (var i = 0; i < game.getBoard().getPieces().length; i++) {
            var piece = game.getBoard().getPieces()[i];
            var button = buttons.get(i);

            if (piece != null) {
                button.setText(piece.toString());
                button.setEnabled(false);
            } else {
                button.setText("");
                button.setEnabled(true);
            }
        }

        if (game.isOver()) {
            for (var button : buttons) {
                button.setEnabled(false);
            }

            var winner = game.getWinner();

            if (winner != null) {
                JOptionPane.showMessageDialog(this, String.format("%s won!", winner.getSide()), "Game over", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Draw", "Game over", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            var playerToMove = game.getPlayerToMove();

            if (playerToMove instanceof ComputerPlayer computerPlayer) {
                game.makeMove(computerPlayer.getMove(game.getBoard()));
            }
        }
    }
}
