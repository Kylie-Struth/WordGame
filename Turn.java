import javax.swing.JOptionPane;
import java.util.Random;

public class Turn {
    private Phrases phrases;
    private GUI gui;

    public Turn(Phrases phrases, GUI gui) {
        this.phrases = phrases;
        this.gui = gui;
    }

    public boolean takeTurn(Players player, Hosts host, String letterGuess) {
        boolean validGuess = false;

        try {
            boolean correctGuess = phrases.findLetters(letterGuess);

            boolean winMoney = new Random().nextBoolean();
            Award award = winMoney ? new Money() : new Physical();
            int winnings = award.displayWinnings(player, correctGuess);

            if (!correctGuess) {
            }

            player.setMoney(player.getMoney() + winnings);
            gui.updatePlayerLabel(player.toString());

            gui.updatePlayingPhraseLabel("Current Phrase: " + Phrases.getPlayingPhrase());

            validGuess = true;

        } catch (MultipleLettersException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return !Phrases.getPlayingPhrase().contains("_");
    }
}
