import java.util.Scanner;

public class Turn {
    private static Numbers numbers;
    private static final int WIN_AMOUNT = 1000;
    private static final int INCORRECT_GUESS_PENALTY = 200;

    public static boolean takeTurn(Players player, Hosts host) {
        Scanner scnr = new Scanner(System.in);

        System.out.println(host.getFirstName() + " " + host.getLastName() +
                " says \"" + player.getFirstName() + (player.getLastName().isEmpty() ? "" : " " + player.getLastName())
                + ", enter your guess for my random number between 0 and 100\"");

        int guess = scnr.nextInt();

        if (numbers == null) {
            numbers = new Numbers();
            numbers.generateNumber();
        }

        boolean guessedCorrectly = numbers.compareNumber(guess);

        if (guessedCorrectly) {

            player.setMoney(player.getMoney() + WIN_AMOUNT);
            System.out.println(player.toString());
        } else {

            player.setMoney(player.getMoney() - INCORRECT_GUESS_PENALTY);
            System.out.println("You lose $" + INCORRECT_GUESS_PENALTY + ".00");
            System.out.println(player.toString());
        }

        return guessedCorrectly;
    }
}