import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    private GameEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new GameEngine(1, 100);
    }

    @Test
    public void testInitialState() {
        assertEquals(0, engine.getAttempts());
        assertFalse(engine.isGameWon());
        assertFalse(engine.hasUserQuit());
        assertFalse(engine.isGameOver());
        assertEquals(10, engine.getMaxAttempts());
    }

    @Test
    public void testCorrectGuess() {
        engine.setTarget(50);
        GuessResult result = engine.makeGuess(50);
        assertTrue(result.isCorrect());
        assertTrue(engine.isGameWon());
        assertEquals(1, engine.getAttempts());
    }

    @Test
    public void testTooLowGuess() {
        engine.setTarget(50);
        GuessResult result = engine.makeGuess(30);
        assertFalse(result.isCorrect());
        assertTrue(result.getMessage().contains("Too low"));
    }

    @Test
    public void testTooHighGuess() {
        engine.setTarget(50);
        GuessResult result = engine.makeGuess(70);
        assertFalse(result.isCorrect());
        assertTrue(result.getMessage().contains("Too high"));
    }

    @Test
    public void testMultipleGuesses() {
        engine.setTarget(50);
        engine.makeGuess(30);
        engine.makeGuess(70);
        GuessResult result = engine.makeGuess(50);
        assertTrue(result.isCorrect());
        assertEquals(3, engine.getAttempts());
    }

    @Test
    public void testReset() {
        engine.setTarget(50);
        engine.makeGuess(50);
        engine.reset();
        assertEquals(0, engine.getAttempts());
        assertFalse(engine.isGameWon());
        assertFalse(engine.hasUserQuit());
        assertFalse(engine.isGameOver());
    }

    @Test
    public void testBoundaries() {
        assertEquals(1, engine.getMin());
        assertEquals(100, engine.getMax());
    }

    @Test
    public void testQuitWithNegativeNumber() {
        engine.setTarget(50);
        GuessResult result = engine.makeGuess(-1);
        assertFalse(result.isCorrect());
        assertTrue(engine.hasUserQuit());
        assertTrue(result.getMessage().contains("Exiting"));
    }

    @Test
    public void testQuitDoesNotIncrementAttempts() {
        engine.setTarget(50);
        engine.makeGuess(-1);
        assertEquals(0, engine.getAttempts());
    }

    // -------------------------
    // HINTS FEATURE TESTS
    // -------------------------

    @Test
    public void testHintsEnabledByDefault() {
        assertTrue(engine.isHintsEnabled());
    }

    @Test
    public void testSetHintsEnabled() {
        engine.setHintsEnabled(false);
        assertFalse(engine.isHintsEnabled());
        engine.setHintsEnabled(true);
        assertTrue(engine.isHintsEnabled());
    }

    @Test
    public void testHintVeryClose() {
        engine.setTarget(50);

        // Need at least 3 attempts before "very close" hint can appear
        engine.makeGuess(60); // attempt 1
        engine.makeGuess(60); // attempt 2
        GuessResult result = engine.makeGuess(55); // attempt 3, diff=5

        assertTrue(result.getHint().contains("You're very close"));
    }

    @Test
    public void testHintGettingWarmer() {
        engine.setTarget(50);

        // Need at least 5 attempts and diff <= 20
        engine.makeGuess(90); // 1
        engine.makeGuess(90); // 2
        engine.makeGuess(90); // 3
        engine.makeGuess(90); // 4
        GuessResult result = engine.makeGuess(65); // 5, diff=15

        assertTrue(result.getHint().contains("Getting warmer"));
    }

    @Test
    public void testNoHintWhenFarAway() {
        engine.setTarget(50);

        // At 5th attempt but diff is huge, should be no hint
        engine.makeGuess(90); // 1
        engine.makeGuess(90); // 2
        engine.makeGuess(90); // 3
        engine.makeGuess(90); // 4
        GuessResult result = engine.makeGuess(1); // 5, diff=49

        assertTrue(result.getHint() == null || result.getHint().isEmpty());
    }

    @Test
    public void testNoHintBeforeThreeAttempts() {
        engine.setTarget(50);

        GuessResult result = engine.makeGuess(55); // attempt 1
        assertTrue(result.getHint() == null || result.getHint().isEmpty());
    }

    @Test
    public void testHintsCanBeDisabled() {
        engine.setTarget(50);
        engine.setHintsEnabled(false);

        engine.makeGuess(60); // 1
        engine.makeGuess(60); // 2
        GuessResult result = engine.makeGuess(55); // 3

        assertTrue(result.getHint() == null || result.getHint().isEmpty());
    }

    // -------------------------
    // MAX ATTEMPTS / GAME OVER TESTS
    // -------------------------

    @Test
    public void testMaxAttemptsReached() {
        engine.setTarget(50);

        // 10 wrong guesses => gameOver
        for (int i = 0; i < 10; i++) {
            engine.makeGuess(1);
        }

        assertTrue(engine.isGameOver());
        assertFalse(engine.isGameWon());
        assertEquals(10, engine.getAttempts());
    }

    @Test
    public void testRemainingAttemptsDecrements() {
        engine.setTarget(50);

        GuessResult result = engine.makeGuess(1); // attempt 1
        assertEquals(9, result.getRemainingAttempts());
    }

    @Test
    public void testGameOverMessageContainsTarget() {
        engine.setTarget(50);

        GuessResult result = null;
        for (int i = 0; i < 10; i++) {
            result = engine.makeGuess(1);
        }

        assertNotNull(result);
        assertTrue(result.getMessage().contains("Game Over"));
        assertTrue(result.getMessage().contains("50"));
        assertEquals(0, result.getRemainingAttempts());
    }

    @Test
    public void testWinBeforeMaxAttempts() {
        engine.setTarget(50);

        for (int i = 0; i < 5; i++) {
            engine.makeGuess(1);
        }

        GuessResult result = engine.makeGuess(50);
        assertTrue(result.isCorrect());
        assertTrue(engine.isGameWon());
        assertFalse(engine.isGameOver());
    }

    @Test
    public void testResetClearsGameOver() {
        engine.setTarget(50);

        for (int i = 0; i < 10; i++) {
            engine.makeGuess(1);
        }
        assertTrue(engine.isGameOver());

        engine.reset();
        assertFalse(engine.isGameOver());
        assertEquals(0, engine.getAttempts());
    }
}

