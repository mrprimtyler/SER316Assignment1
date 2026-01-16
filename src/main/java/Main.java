import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Welcome to the Number Guessing Game! ===");

        System.out.println("Good luck!");

        // Version 1.1 - Added quit functionality


        Scanner scanner = new Scanner(System.in);
        GameEngine engine = new GameEngine(1, 100);
        GameUI ui = new GameUI(engine, scanner);

        boolean playAgain = true;
        while (playAgain) {
            ui.start();

            if (!engine.hasUserQuit()) {
                System.out.print("Play again? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                playAgain = response.equals("y") || response.equals("yes");

                if (playAgain) {
                    engine.reset();
                }
            } else {
                playAgain = false;
            }
        }

        System.out.println("Thanks for playing!");
    }
}
