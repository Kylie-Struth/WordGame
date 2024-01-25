public class Money implements Award {
    private static final int WIN_AMOUNT = 1000;
    private static final int INCORRECT_GUESS_PENALTY = 200;

    @Override
    public int displayWinnings(Players player, boolean correctGuess) {
        if (correctGuess) {
            System.out.println(player.getFirstName() + ", yes, that letter is in the phrase! You won $" + WIN_AMOUNT + ".00");
            return WIN_AMOUNT;
        } else {
            System.out.println(player.getFirstName() + ", sorry, that letter is NOT in the phrase! You lost $" + INCORRECT_GUESS_PENALTY + ".00");
            return -INCORRECT_GUESS_PENALTY;
        }
    }
}
