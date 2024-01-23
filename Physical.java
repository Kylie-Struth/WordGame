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
            System.out.println(player.getFirstName() + ", congratulations! You won a " + prize + ".");
        } else {
            System.out.println(player.getFirstName() + ", sorry, that is incorrect! If you had gotten it correct, you COULD HAVE WON a " + prize + ".");
        }

        return 0;
    }
}
