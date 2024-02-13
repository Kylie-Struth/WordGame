import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.io.File;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Physical implements Award {
    private String[] physicalPrizes = {"TV", "Vacation Package", "Home Appliance", "Gaming Console", "Gift Card"};
    private Map<String, String> prizeImages;
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 200;
    private static final String CORRECT_SOUND_PATH = "sounds/correct.wav";
    private static final String INCORRECT_SOUND_PATH = "sounds/incorrect.wav";

    public Physical() {
        // Initialize prize images
        prizeImages = new HashMap<>();
        prizeImages.put("TV", "imgs/tv.jpg");
        prizeImages.put("Vacation Package", "imgs/vacationPackage.jpg");
        prizeImages.put("Home Appliance", "imgs/homeAppliance.jpg");
        prizeImages.put("Gaming Console", "imgs/gamingConsole.jpg");
        prizeImages.put("Gift Card", "imgs/giftCard.jpg");
    }

    public String getRandomPrize() {
        Random random = new Random();
        int prizeIndex = random.nextInt(physicalPrizes.length);
        return physicalPrizes[prizeIndex];
    }

    @Override
    public int displayWinnings(Players player, boolean correctGuess) {
        // Play sound effect
        if (correctGuess) {
            playSound(CORRECT_SOUND_PATH);
        } else {
            playSound(INCORRECT_SOUND_PATH);
            return 0; // Return without showing prize for incorrect guess
        }

        String prize = getRandomPrize();
        String prizeImagePath = prizeImages.get(prize);

        if (prizeImagePath != null) {
            // Load image and display prize
            ImageIcon originalIcon = new ImageIcon(prizeImagePath);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JOptionPane.showMessageDialog(null, player.getFirstName() +
                            ", yes, that letter is in the phrase! You won a " + prize + ".", "Congratulations!",
                    JOptionPane.INFORMATION_MESSAGE, scaledIcon);
        } else {
            JOptionPane.showMessageDialog(null, player.getFirstName() +
                            ", yes, that letter is in the phrase! You won a " + prize + ".", "Congratulations!",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        return 0;
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