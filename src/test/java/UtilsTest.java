import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    public void testRandomIntInRange() {
        for (int i = 0; i < 100; i++) {
            int result = Utils.randomInt(1, 10);
            assertTrue(result >= 1 && result <= 10);
        }
    }

    @Test
    public void testRandomIntSingleValue() {
        int result = Utils.randomInt(5, 5);
        assertEquals(5, result);
    }

    @Test
    public void testRandomIntIncludesMax() {
        boolean foundMax = false;
        for (int i = 0; i < 1000; i++) {
            if (Utils.randomInt(1, 2) == 2) {
                foundMax = true;
                break;
            }
        }
        assertTrue(foundMax, "randomInt should be able to return max value");
    }

    @Test
    public void testRandomIntIncludesMin() {
        boolean foundMin = false;
        for (int i = 0; i < 1000; i++) {
            if (Utils.randomInt(1, 2) == 1) {
                foundMin = true;
                break;
            }
        }
        assertTrue(foundMin, "randomInt should be able to return min value");
    }
}
