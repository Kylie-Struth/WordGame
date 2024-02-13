import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
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
    private JTextArea messageTextArea;
    private int currentPlayerIndex = 0;
    private Turn turn;
    private Map<String, JLabel> moneyLabels;
    private JScrollPane scrollPane;
    private static final String WINNER_SOUND_PATH = "sounds/winner.aiff";
    private static final String INVALID_SOUND_PATH = "sounds/invalid.wav";
    private static final String DINOSAUR_IMAGE_PATH = "imgs/dinosaur.png";
    private static final int DINOSAUR_WIDTH = 100;
    private static final int DINOSAUR_HEIGHT = 100;
    private static final int ANIMATION_DELAY = 25;
    private static final int MOVEMENT_STEP = 1;
    private Timer dinosaurTimer;
    private boolean moveRight = true;
    private int dinosaurX = 0;
    private JLabel dinosaurLabel;

    public GUI() {
        playersList = new ArrayList<>();
        host = new Hosts("", "");
        turn = new Turn(new Phrases(), this);

        setTitle("Word Game");
        setSize(400, 400);
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

        JMenuItem attributionMenuItem = new JMenuItem("Attribution");
        attributionMenuItem.addActionListener(e -> showAttribution());
        aboutMenu.add(attributionMenuItem);

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

        dinosaurLabel = new JLabel();
        ImageIcon dinosaurIcon = new ImageIcon(DINOSAUR_IMAGE_PATH);
        Image dinosaurImage = dinosaurIcon.getImage().getScaledInstance(DINOSAUR_WIDTH, DINOSAUR_HEIGHT, Image.SCALE_SMOOTH);
        dinosaurLabel.setIcon(new ImageIcon(dinosaurImage));
        add(dinosaurLabel);

        setLayout(new FlowLayout());
        setVisible(true);

        // Timer for dinosaur animation
        dinosaurTimer = new Timer(ANIMATION_DELAY, e -> {
            if (moveRight) {
                dinosaurX += MOVEMENT_STEP;
                if (dinosaurX >= getWidth() - DINOSAUR_WIDTH) {
                    moveRight = false;
                }
            } else {
                dinosaurX -= MOVEMENT_STEP;
                if (dinosaurX <= 0) {
                    moveRight = true;
                }
            }
            dinosaurLabel.setLocation(dinosaurX, getHeight() - DINOSAUR_HEIGHT - 50);
        });
        dinosaurTimer.start();
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

            if (letterGuess == null) {
                return;
            }

            if (letterGuess.length() == 1 && Character.isLetter(letterGuess.charAt(0))) {
                boolean correctGuess = turn.takeTurn(currentPlayer, host, letterGuess);

                currentPlayerIndex = (currentPlayerIndex + 1) % playersList.size();

                gameSolved = !Phrases.getPlayingPhrase().contains("_");
                if (gameSolved) {
                    updatePlayingPhraseLabel("You solved the puzzle and won the game!");
                    playSound(WINNER_SOUND_PATH);
                }
            } else {
                playSound(INVALID_SOUND_PATH);
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a single letter.");
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

    private void showAttribution() {
        JOptionPane.showMessageDialog(this, "Images Attribution: https://unsplash.com/photos/black-sony-ps-4-game-controller-EXU2EFzd4uY, https://unsplash.com/photos/black-and-gray-blender-ZN_86cZrSN0, https://unsplash.com/photos/a-flat-screen-tv-sitting-on-top-of-a-wooden-table-cF6Le-0viHY https://unsplash.com/photos/person-using-laptop-computer-holding-card-Q59HmzK38eQ https://unsplash.com/photos/a-large-swimming-pool-surrounded-by-palm-trees-_pPHgeHz1uk, <a href=\"https://www.freepik.com/free-vector/happy-cartoon-dinosaur-character-smiling_58468188.htm#query=dino&position=43&from_view=keyword&track=sph&uuid=faab5fba-2765-43e8-a7d2-e1ccdba2bd38#position=43&query=dino, Image by brgfx</a> on Freepik\n" +
                "Sounds Attribution: https://freesound.org/s/587252/, https://freesound.org/s/587253/, https://freesound.org/s/97980/, https://freesound.org/s/529384/");
    }

    private void playSound(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
