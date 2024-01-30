import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI extends JFrame {
    private List<Players> playersList;
    private Hosts host;
    private JLabel playersLabel;
    private JLabel hostLabel;
    private JLabel playingPhraseLabel;
    private int currentPlayerIndex = 0;
    private Turn turn;
    private Map<String, JLabel> moneyLabels;

    public GUI() {
        playersList = new ArrayList<>();
        host = new Hosts("", "");
        turn = new Turn(new Phrases(), this);

        setTitle("Word Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        playersLabel = new JLabel("Current Players: " + getPlayersNames());
        add(playersLabel);

        JButton addPlayerButton = new JButton("Add Player");
        addPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayer();
            }
        });
        add(addPlayerButton);

        hostLabel = new JLabel("Current Host: " + host.getFirstName() + " " + host.getLastName());
        add(hostLabel);

        JButton addHostButton = new JButton("Add Host");
        addHostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHost();
            }
        });
        add(addHostButton);

        playingPhraseLabel = new JLabel("Playing Phrase: " + Phrases.getPlayingPhrase());
        add(playingPhraseLabel);

        JButton startTurnButton = new JButton("Start Turn");
        startTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTurn();
            }
        });
        add(startTurnButton);

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
        String firstName = JOptionPane.showInputDialog("Enter the first name of the new host:");
        String lastName = JOptionPane.showInputDialog("Enter the last name of the new host:");
        host = new Hosts(firstName, lastName);
        String gamePhrase = JOptionPane.showInputDialog("Enter the game phrase for the host:");
        Phrases.setGamePhrase(gamePhrase);
        hostLabel.setText("Current Host: " + host.getFirstName() + " " + host.getLastName());
        updatePlayingPhraseLabel();
    }

    private void startTurn() {
        boolean gameSolved = false;

        while (!gameSolved) {
            Players currentPlayer = playersList.get(currentPlayerIndex);
            String letterGuess = JOptionPane.showInputDialog("Player " + currentPlayer.getFirstName() +
                    ", enter your guess for a letter in the phrase:");

            boolean correctGuess = turn.takeTurn(currentPlayer, host, letterGuess);

            currentPlayerIndex = (currentPlayerIndex + 1) % playersList.size();

            gameSolved = !Phrases.getPlayingPhrase().contains("_");
            if (gameSolved) {
                updatePlayingPhraseLabel();
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
                updatePlayingPhraseLabel();
                currentPlayerIndex = 0;
            }
        } else {
            System.exit(0);
        }
    }

    public void updatePlayingPhraseLabel() {
        playingPhraseLabel.setText("Playing Phrase: " + Phrases.getPlayingPhrase());
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
