import java.util.HashSet;
import java.util.Set;

public class Phrases {
    private static String gamePhrase;
    private static String playingPhrase;

    public static void setGamePhrase(String phrase) {
        gamePhrase = phrase;
        playingPhrase = phrase.replaceAll("[a-zA-Z]", "_");
    }

    public static String getGamePhrase() {
        return gamePhrase;
    }

    public static String getPlayingPhrase() {
        return playingPhrase;
    }

    public static boolean findLetters(String letters) throws MultipleLettersException {
        Set<Character> uniqueLetters = new HashSet<>();
        boolean anyCorrectGuess = false;

        for (int i = 0; i < letters.length(); i++) {
            char guess = Character.toLowerCase(letters.charAt(i));

            if (letters.length() > 1) {
                throw new MultipleLettersException("Please enter only one letter at a time.");
            }

            if (!Character.isLetter(guess)) {
                throw new IllegalArgumentException("Invalid input. Please enter a letter.");
            }

            if (uniqueLetters.contains(guess)) {
                throw new MultipleLettersException("You've already guessed the letter '" + guess + "'.");
            }

            uniqueLetters.add(guess);

            boolean letterFound = false;

            for (int j = 0; j < gamePhrase.length(); j++) {
                char currentChar = Character.toLowerCase(gamePhrase.charAt(j));

                if (currentChar == guess) {
                    if (playingPhrase.charAt(j) == '_') {
                        playingPhrase = playingPhrase.substring(0, j) + currentChar + playingPhrase.substring(j + 1);
                        letterFound = true;
                        anyCorrectGuess = true;
                    }
                }
            }
        }

        return anyCorrectGuess;
    }
}
