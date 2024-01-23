import java.util.Random;
import java.util.Scanner;

public class Turn {
    private static Numbers numbers;

    public static boolean takeTurn(Players[] players, Hosts host) {
        Scanner scnr = new Scanner(System.in);

        if (numbers == null) {
            numbers = new Numbers();
            numbers.generateNumber();
        }

        for (int i = 0; i < players.length; i++) {
            Players player = players[i];

            System.out.println(host.getFirstName() + " " + host.getLastName() +
                    " says \"" + player.getFirstName() + (player.getLastName().isEmpty() ? "" : " " + player.getLastName())
                    + ", enter your guess for my random number between 0 and 100\"");

            int guess = scnr.nextInt();

            boolean guessedCorrectly = numbers.compareNumber(guess);

            Random random = new Random();
            boolean winMoney = random.nextBoolean();

            if (winMoney) {
                Money money = new Money();
                int moneyWinnings = money.displayWinnings(player, guessedCorrectly);
                player.setMoney(player.getMoney() + moneyWinnings);
            } else {
                Physical physical = new Physical();
                int physicalWinnings = physical.displayWinnings(player, guessedCorrectly);
                player.setMoney(player.getMoney() + physicalWinnings);
            }

            System.out.println(player.toString());

            if (guessedCorrectly) {
                return true;
            }
        }
        return false;
    }
}
