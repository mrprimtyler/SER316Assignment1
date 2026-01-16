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
        assertTrue(result.getMessage().contains("Too low!"));
    }

    @Test
    public void testTooHighGuess() {
        engine.setTarget(50);
        GuessResult result = engine.makeGuess(70);
        assertFalse(result.isCorrect());
        assertTrue(result.getMessage().contains("Too high!"));
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
        
    @Test
    public void testHintVeryClose() {
        engine.setTarget(50);
        engine.makeGuess(60);
        engine.makeGuess(60);
        GuessResult result = engine.makeGuess(55);
        assertTrue(result.getMessage().contains("HINT: You're very close!"));
    }

    @Test
    public void testHintGettingWarmer() {
        engine.setTarget(50);
        for (int i = 0; i < 5; i++) {
            engine.makeGuess(90);
        }
        GuessResult result = engine.makeGuess(65);
        assertTrue(result.getMessage().contains("HINT: Getting warmer!"));
    }

    @Test
    public void testNoHintWhenFarAway() {
        engine.setTarget(50);
        for (int i = 0; i < 5; i++) {
            engine.makeGuess(90);
        }
        GuessResult result = engine.makeGuess(1);
        assertFalse(result.getMessage().contains("HINT"));
    }

    @Test
    public void testNoHintBeforeThreeAttempts() {
        engine.setTarget(50);
        GuessResult result = engine.makeGuess(55);
        assertFalse(result.getMessage().contains("HINT"));
    }

    @Test
    public void testHintsCanBeDisabled() {
        engine.setTarget(50);
        engine.setHintsEnabled(false);
        for (int i = 0; i < 3; i++) {
            engine.makeGuess(60);
        }
        GuessResult result = engine.makeGuess(55);
        assertFalse(result.getMessage().contains("HINT"));
    }

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
    public void testHintFieldAccessor() {
        engine.setTarget(50);
        for (int i = 0; i < 3; i++) {
            engine.makeGuess(60);
        }
        GuessResult result = engine.makeGuess(55);
        assertFalse(result.getHint().isEmpty());
    }
}
