import java.util.Scanner;

public class GamePlay {
    private static Players player;

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        Hosts host = new Hosts("John", "Doe");

        host.randomizeNum();

        System.out.print("What is your first name? ");
        String playerName = scnr.nextLine();

        System.out.print("Would you like to enter a last name? Leave blank if not. ");
        String lastName = scnr.nextLine();

        player = new Players(playerName, lastName);

        Turn turn = new Turn();

        boolean playAgain = true;

        while (playAgain) {

            boolean guessedCorrectly = false;

            while (!guessedCorrectly) {

                guessedCorrectly = turn.takeTurn(player, host);

                if (guessedCorrectly) {
                    System.out.print("Play another game? (y or n): ");
                    char playAnother = scnr.next().charAt(0);

                    if (playAnother == 'y' || playAnother == 'Y') {

                        host.randomizeNum();
                    } else {
                        playAgain = false;
                    }
                }
            }
        }

        scnr.close();
    }
}
