import javax.swing.JOptionPane;
import java.util.Random;

public class Physical implements Award {
    private String[] physicalPrizes = {"TV", "Vacation Package", "Home Appliance", "Gaming Console", "Gift Card"};

    public String getRandomPrize() {
        Random random = new Random();
        int prizeIndex = random.nextInt(physicalPrizes.length);
        return physicalPrizes[prizeIndex];
    }

    @Override
    public int displayWinnings(Players player, boolean correctGuess) {
        String prize = getRandomPrize();

        if (correctGuess) {
            JOptionPane.showMessageDialog(null, player.getFirstName() +
                    ", yes, that letter is in the phrase! You won a " + prize + ".");
        } else {
            JOptionPane.showMessageDialog(null, player.getFirstName() +
                    ", sorry, that letter is NOT in the phrase! You could have won a " + prize + ".");
        }

        return 0;
    }
}