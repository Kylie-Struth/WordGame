import java.util.Scanner;

public class GamePlay {
    private static Person player;

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        System.out.print("What is your first name? ");
        String playerName = scnr.nextLine();

        System.out.print("Would you like to enter a last name? Leave blank if not. ");
        String lastName = scnr.nextLine();

        if (lastName.isEmpty()) {
            player = new Person(playerName);
        } else {
            player = new Person(playerName, lastName);
        }

        Numbers numberGame = new Numbers();
        numberGame.generateNumber();

        System.out.print(player.getFirstName());
        if (!player.getLastName().isEmpty()) {
            System.out.print(" " + player.getLastName());
        }
        System.out.println(", guess what number I picked between 0 and 100.");

        boolean guessedCorrectly = false;

        while (!guessedCorrectly) {
            System.out.print("Enter your guess: ");
            int guess = scnr.nextInt();

            guessedCorrectly = numberGame.compareNumber(guess);
        }

        scnr.close();
    }
}
