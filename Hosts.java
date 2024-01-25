import java.util.Scanner;

public class Hosts extends Person {
    private static String gamePhrase;

    public Hosts(String firstName, String lastName) {
        super(firstName, lastName);
        enterPhrase(firstName);
    }

    private void enterPhrase(String hostName) {
        Scanner scnr = new Scanner(System.in);
        System.out.print("Enter a phrase for the players to guess: ");
        gamePhrase = scnr.nextLine();
        Phrases.setGamePhrase(gamePhrase);
    }
}
