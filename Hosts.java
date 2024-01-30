import javax.swing.*;

public class Hosts extends Person {
    private static String gamePhrase;

    public Hosts(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void enterPhrase() {
        gamePhrase = JOptionPane.showInputDialog("Enter a phrase for the players to guess:");
        Phrases.setGamePhrase(gamePhrase);
    }

    public static String getGamePhrase() {
        return gamePhrase;
    }
}
