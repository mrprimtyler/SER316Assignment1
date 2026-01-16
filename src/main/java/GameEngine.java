public class GameEngine {
    private final int min;
    private final int max;
    private int target;
    private int attempts;
    private boolean gameWon;
    private boolean userQuit;
    private boolean hintsEnabled;

    public GameEngine(int min, int max) {
        this.min = min;
        this.max = max;
        this.attempts = 0;
        this.gameWon = false;
        this.userQuit = false;
        this.hintsEnabled = true;
        reset();
    }

    public GuessResult makeGuess(int guess) {
        // Quit handling (negative number)
        if (guess < 0) {
            userQuit = true;
            return new GuessResult(false, "Exiting game...", attempts);
        }

        attempts++;

        // Correct guess
        if (guess == target) {
            gameWon = true;
            return new GuessResult(true,
                    "Correct! You guessed it in " + attempts + " attempts.",
                    attempts);
        }

        // Not correct: build message + optional hint
        GuessResult result;
        if (guess < target) {
            result = new GuessResult(false, "Too low! Try a higher number.", attempts);
        } else {
            result = new GuessResult(false, "Too high! Try a lower number.", attempts);
        }

        // Hints (only if enabled)
        String hint = getHint(guess);
        if (hint != null && !hint.isEmpty()) {
            result.setHint(hint);
        }

        return result;
    }

    public void reset() {
        target = Utils.randomInt(min, max);
        attempts = 0;
        gameWon = false;
        userQuit = false;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean hasUserQuit() {
        return userQuit;
    }

    public int getAttempts() {
        return attempts;
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

