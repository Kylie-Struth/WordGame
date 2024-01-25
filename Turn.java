import java.util.Scanner;
import java.util.Random;

public class Turn {
    private Phrases phrases;

    public Turn(Phrases phrases) {
        this.phrases = phrases;
    }

    public boolean takeTurn(Players player, Hosts host, Scanner scnr) {
        System.out.println("The phrase to guess is: " + Phrases.getPlayingPhrase());
        System.out.println(host.getFirstName() + " " + host.getLastName() +
                " says \"" + player.getFirstName() + (player.getLastName().isEmpty() ? "" : " " + player.getLastName())
                + ", enter your guess for a letter in the phrase\"");

        boolean validGuess = false;
        while (!validGuess) {
            String letterGuess = scnr.nextLine().toLowerCase(); // Convert to lowercase for case-insensitivity

            try {
                boolean correctGuess = phrases.findLetters(letterGuess);

                Random random = new Random();
                boolean winMoney = random.nextBoolean();

                Award award = winMoney ? new Money() : new Physical();
                int winnings = award.displayWinnings(player, correctGuess);
                player.setMoney(player.getMoney() + winnings);

                System.out.println(player.toString());
                validGuess = true;

            } catch (MultipleLettersException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return !Phrases.getPlayingPhrase().contains("_");
    }
}
