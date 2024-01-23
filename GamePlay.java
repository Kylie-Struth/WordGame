import java.util.Scanner;

public class GamePlay {
    private static Players[] currentPlayers = new Players[3];

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        Hosts host = new Hosts("John", "Doe");

        // Loop to create 3 players and add them to the currentPlayers array
        for (int i = 0; i < currentPlayers.length; i++) {
            System.out.print("Enter player " + (i + 1) + "'s first name: ");
            String playerName = scnr.nextLine();

            System.out.print("Would you like to enter a last name? Leave blank if not. ");
            String lastName = scnr.nextLine();

            currentPlayers[i] = new Players(playerName, lastName);
        }

        boolean playAgain = true;
        Turn turn = new Turn();

        while (playAgain) {
            for (Players currentPlayer : currentPlayers) {
                boolean guessedCorrectly = turn.takeTurn(currentPlayers, host);

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
