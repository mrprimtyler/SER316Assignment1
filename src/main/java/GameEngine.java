public class GameEngine {
    private static final int MAX_ATTEMPTS = 10;

    private final int min;
    private final int max;
    private int target;
    private int attempts;
    private boolean gameWon;
    private boolean userQuit;
    private boolean gameOver;

    public GameEngine(int min, int max) {
        this.min = min;
        this.max = max;
        reset();
    }

    public GuessResult makeGuess(int guess) {
        // Quit if user enters a negative number
        if (guess < 0) {
            userQuit = true;
            return new GuessResult(false, "Exiting game...", attempts);
        }

        if (gameWon || gameOver) {
            GuessResult r = new GuessResult(false, "Game is over. Reset to play again.", attempts);
            r.setRemainingAttempts(Math.max(0, MAX_ATTEMPTS - attempts));
            return r;
        }

        attempts++;

        if (guess == target) {
            gameWon = true;
            GuessResult r = new GuessResult(true,
                    "Correct! You guessed it in " + attempts + " attempts.", attempts);
            r.setRemainingAttempts(Math.max(0, MAX_ATTEMPTS - attempts));
            return r;
        }

        if (attempts >= MAX_ATTEMPTS) {
            gameOver = true;
            GuessResult r = new GuessResult(false,
                    "Game Over! You've used all " + MAX_ATTEMPTS + " attempts. The number was " + target + ".", attempts);
            r.setRemainingAttempts(0);
            return r;
        }

        int remaining = MAX_ATTEMPTS - attempts;
        GuessResult r;
        if (guess < target) {
            r = new GuessResult(false, "Too low!", attempts);
        } else {
            r = new GuessResult(false, "Too high!", attempts);
        }
        r.setRemainingAttempts(remaining);
        return r;
    }

    public void reset() {
        target = Utils.randomInt(min, max);
        attempts = 0;
        gameWon = false;
        userQuit = false;
        gameOver = false;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean hasUserQuit() {
        return userQuit;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    // For testing purposes only
    protected void setTarget(int target) {
        this.target = target;
    }

    protected int getTarget() {
        return target;
    }
}

