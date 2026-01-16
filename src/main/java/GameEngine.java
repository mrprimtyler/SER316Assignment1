public class GameEngine {
    private static final int MAX_ATTEMPTS = 10;

    private final int min;
    private final int max;

    private int target;
    private int attempts;
    private boolean gameWon;
    private boolean userQuit;
    private boolean gameOver;

    private boolean hintsEnabled;

    public GameEngine(int min, int max) {
        this.min = min;
        this.max = max;
        this.hintsEnabled = true;
        reset();
    }

    public GuessResult makeGuess(int guess) {
        // Quit handling (negative number)
        if (guess < 0) {
            userQuit = true;
            return new GuessResult(false, "Exiting game...", attempts);
        }

        // If already ended, don't allow more guesses
        if (gameWon || gameOver) {
            GuessResult r = new GuessResult(false, "Game is over. Reset to play again.", attempts);
            r.setRemainingAttempts(Math.max(0, MAX_ATTEMPTS - attempts));
            return r;
        }

        // This guess counts
        attempts++;

        // Correct guess
        if (guess == target) {
            gameWon = true;
            GuessResult r = new GuessResult(true,
                    "Correct! You guessed it in " + attempts + " attempts.",
                    attempts);
            r.setRemainingAttempts(Math.max(0, MAX_ATTEMPTS - attempts));
            return r;
        }

        // Max attempts reached (after increment)
        if (attempts >= MAX_ATTEMPTS) {
            gameOver = true;
            GuessResult r = new GuessResult(false,
                    "Game Over! You've used all " + MAX_ATTEMPTS + " attempts. The number was " + target + ".",
                    attempts);
            r.setRemainingAttempts(0);
            return r;
        }

        // Too low / too high
        GuessResult r;
        if (guess < target) {
            r = new GuessResult(false, "Too low! Try a higher number.", attempts);
        } else {
            r = new GuessResult(false, "Too high! Try a lower number.", attempts);
        }

        // Remaining attempts
        int remaining = MAX_ATTEMPTS - attempts;
        r.setRemainingAttempts(remaining);

        // Optional hint
        String hint = getHint(guess);
        if (hint != null && !hint.isEmpty()) {
            r.setHint(hint);
        }

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

    public boolean isHintsEnabled() {
        return hintsEnabled;
    }

    public void setHintsEnabled(boolean enabled) {
        this.hintsEnabled = enabled;
    }

    private String getHint(int guess) {
        if (!hintsEnabled) {
            return "";
        }

        int diff = Math.abs(target - guess);
        if (attempts >= 3 && diff <= 10) {
            return " HINT: You're very close!";
        } else if (attempts >= 5 && diff <= 20) {
            return " HINT: Getting warmer!";
        }
        return "";
    }

    // For testing purposes only
    protected void setTarget(int target) {
        this.target = target;
    }

    protected int getTarget() {
        return target;
    }
}

