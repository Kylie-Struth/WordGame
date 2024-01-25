import java.util.Scanner;

public class GamePlay {
    private static Players[] currentPlayers = new Players[3];

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        for (int i = 0; i < currentPlayers.length; i++) {
            System.out.print("Enter player " + (i + 1) + "'s first name: ");
            String playerName = scnr.nextLine();

            System.out.print("Would you like to enter a last name? Leave blank if not. ");
            String lastName = scnr.nextLine();

            currentPlayers[i] = new Players(playerName, lastName);
        }
        Hosts host = new Hosts("John", "Doe");
        Phrases phrases = new Phrases();
        Turn turn = new Turn(phrases);

        boolean playAgain = true;

        while (playAgain) {
            for (Players currentPlayer : currentPlayers) {
                boolean guessedCorrectly = turn.takeTurn(currentPlayer, host, scnr);

                if (guessedCorrectly) {
                    System.out.print("You solved the puzzle and won the game!\nPlay another game? (y or n): ");
                    char playAnother = scnr.next().charAt(0);

                    if (playAnother == 'y' || playAnother == 'Y') {
                        scnr.nextLine();
                        System.out.print("Enter the phrase for players to guess: ");
                        phrases.setGamePhrase(scnr.nextLine());
                    } else {
                        playAgain = false;
                    }
                }
            }
        }

        scnr.close();
    }
}
