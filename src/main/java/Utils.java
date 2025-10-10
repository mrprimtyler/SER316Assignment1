import java.util.Random;
import java.util.Scanner;

public class Utils {
    private static final Random random = new Random();

    public static int randomInt(int min, int max) {
        return random.nextInt((max - min + 1)) + min;  // Fixed: inclusive range [min, max]
    }

    public static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }
}
