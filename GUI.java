import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.InputEvent;

public class GUI extends JFrame {
    private List<Players> playersList;
    private Hosts host;
    private JLabel playersLabel;
    private JLabel hostLabel;
    private JLabel playingPhraseLabel;
    private JTextArea messageTextArea;
    private int currentPlayerIndex = 0;
    private Turn turn;
    private Map<String, JLabel> moneyLabels;
    private JScrollPane scrollPane;

    public GUI() {
        playersList = new ArrayList<>();
        host = new Hosts("", "");
        turn = new Turn(new Phrases(), this);

        setTitle("Word Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(gameMenu);

        JMenuItem addPlayerMenuItem = new JMenuItem("Add Player");
        addPlayerMenuItem.addActionListener(e -> addPlayer());
        gameMenu.add(addPlayerMenuItem);

        JMenuItem addHostMenuItem = new JMenuItem("Add Host");
        addHostMenuItem.addActionListener(e -> addHost());
        gameMenu.add(addHostMenuItem);

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(aboutMenu);

        JMenuItem layoutMenuItem = new JMenuItem("Layout");
        layoutMenuItem.addActionListener(e -> showLayoutPopup());
        aboutMenu.add(layoutMenuItem);

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();
        KeyStroke keyStrokeG = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK);
        inputMap.put(keyStrokeG, "openGameMenu");
        actionMap.put("openGameMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameMenu.doClick();
            }
        });

        KeyStroke keyStrokeA = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK);
        inputMap.put(keyStrokeA, "openAboutMenu");
        actionMap.put("openAboutMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutMenu.doClick();
            }
        });

        playersLabel = new JLabel("Current Players: " + getPlayersNames());
        add(playersLabel);

        JButton startTurnButton = new JButton("Start Turn");
        startTurnButton.addActionListener(e -> startTurn());
        add(startTurnButton);

        playingPhraseLabel = new JLabel("Playing Phrase: " + Phrases.getPlayingPhrase());
        add(playingPhraseLabel);

        messageTextArea = new JTextArea();
        messageTextArea.setEditable(false);

        scrollPane = new JScrollPane(messageTextArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        add(scrollPane);

        JCheckBox saveMessagesCheckBox = new JCheckBox("Save Messages");
        saveMessagesCheckBox.setToolTipText("Check to save messages, uncheck to clear messages for each new game");
        saveMessagesCheckBox.addActionListener(e -> updateSaveMessages(saveMessagesCheckBox.isSelected()));
        add(saveMessagesCheckBox);

        moneyLabels = new HashMap<>();

        setLayout(new FlowLayout());
        setVisible(true);
    }

    private String getPlayersNames() {
        StringBuilder names = new StringBuilder();
        for (Players player : playersList) {
            names.append(player.getFirstName()).append(" ").append(player.getLastName()).append(", ");
        }
        return names.toString();
    }

    private void addPlayer() {
        String firstName = JOptionPane.showInputDialog("Enter the first name of the new player:");
        String lastName = JOptionPane.showInputDialog("Enter the last name of the new player:");
        Players newPlayer = new Players(firstName, lastName);
        playersList.add(newPlayer);
        playersLabel.setText("Current Players: " + getPlayersNames());
    }

    private void addHost() {
        if (hostLabel == null) {
            hostLabel = new JLabel();
            add(hostLabel);
        }

        String firstName = JOptionPane.showInputDialog(this, "Enter the first name of the new host:");
        String lastName = JOptionPane.showInputDialog(this, "Enter the last name of the new host:");
        host = new Hosts(firstName, lastName);

        String gamePhrase = JOptionPane.showInputDialog(this, "Enter the game phrase for the host:");
        Phrases.setGamePhrase(gamePhrase);
        hostLabel.setText("Current Host: " + host.getFirstName() + " " + host.getLastName());
        updatePlayingPhraseLabel("New game started. Enter your guesses!");
    }

    private void startTurn() {
        boolean gameSolved = false;

        while (!gameSolved) {
            Players currentPlayer = playersList.get(currentPlayerIndex);
            String letterGuess = JOptionPane.showInputDialog(this, "Player " + currentPlayer.getFirstName() +
                    ", enter your guess for a letter in the phrase:");

            boolean correctGuess = turn.takeTurn(currentPlayer, host, letterGuess);

            currentPlayerIndex = (currentPlayerIndex + 1) % playersList.size();

            gameSolved = !Phrases.getPlayingPhrase().contains("_");
            if (gameSolved) {
                updatePlayingPhraseLabel("You solved the puzzle and won the game!");
            }
        }

        int option = JOptionPane.showConfirmDialog(
                null,
                "You solved the puzzle and won the game!\nPlay another game?",
                "Congratulations!",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            String newPhrase = JOptionPane.showInputDialog("Enter the new playing phrase:");
            if (newPhrase != null && !newPhrase.isEmpty()) {
                Phrases.setGamePhrase(newPhrase);
                updatePlayingPhraseLabel("New game started. Enter your guesses!");
                currentPlayerIndex = 0;
            }
        } else {
            System.exit(0);
        }
    }

    private void showLayoutPopup() {
        updatePlayingPhraseLabel("I chose the layout to provide a simple and clean user interface.");
    }

    public void updatePlayingPhraseLabel(String message) {
        messageTextArea.append(message + "\n");
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    private void updateSaveMessages(boolean saveMessages) {
        if (!saveMessages) {
            messageTextArea.setText("");
        }
    }

    public void updatePlayerLabel(String playerInfo) {
        String[] playerInfoArray = playerInfo.split(":");
        String playerName = playerInfoArray[0].trim();

        JLabel moneyLabel = moneyLabels.get(playerName);

        if (moneyLabel == null) {
            moneyLabel = new JLabel(playerInfo);
            moneyLabels.put(playerName, moneyLabel);
            add(moneyLabel);
        } else {
            moneyLabel.setText(playerInfo);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
