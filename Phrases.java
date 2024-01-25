class Phrases {
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

    public static boolean findLetters(String letter) throws MultipleLettersException {
        if (letter.length() != 1) {
            throw new MultipleLettersException();
        }

        char guess = Character.toLowerCase(letter.charAt(0));

        if (!Character.isLetter(guess)) {
            throw new IllegalArgumentException("Invalid input. Please enter a letter.");
        }

        boolean letterFound = false;

        for (int i = 0; i < gamePhrase.length(); i++) {
            char currentChar = Character.toLowerCase(gamePhrase.charAt(i));

            if (currentChar == guess) {
                if (playingPhrase.charAt(i) == '_') {
                    playingPhrase = playingPhrase.substring(0, i) + currentChar + playingPhrase.substring(i + 1);
                    letterFound = true;
                }
            }
        }

        return letterFound;
    }
}
