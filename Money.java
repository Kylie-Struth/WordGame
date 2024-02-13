import javax.swing.JOptionPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Money implements Award {
    private static final int WIN_AMOUNT = 1000;
    private static final int INCORRECT_GUESS_PENALTY = 200;
    private static final String CORRECT_SOUND_PATH = "sounds/correct.wav";
    private static final String INCORRECT_SOUND_PATH = "sounds/incorrect.wav";

    @Override
    public int displayWinnings(Players player, boolean correctGuess) {
        // Play sound effect
        if (correctGuess) {
            playSound(CORRECT_SOUND_PATH);
        } else {
            playSound(INCORRECT_SOUND_PATH);
        }

        if (correctGuess) {
            JOptionPane.showMessageDialog(null, player.getFirstName() +
                    ", yes, that letter is in the phrase! You won $" + WIN_AMOUNT + ".00");
            return WIN_AMOUNT;
        } else {
            JOptionPane.showMessageDialog(null, player.getFirstName() +
                    ", sorry, that letter is NOT in the phrase! You could have won $1000.00.");
            return -INCORRECT_GUESS_PENALTY;
        }
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
}