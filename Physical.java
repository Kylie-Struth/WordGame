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
            System.out.println(player.getFirstName() + ", yes, that letter is in the phrase! You won a " + prize + ".");
        } else {
            System.out.println(player.getFirstName() + ", sorry, that letter is NOT in the phrase! If you had gotten it correct, you COULD HAVE WON a " + prize + ".");
        }

        return 0; // You might want to modify this based on your actual logic
    }
}