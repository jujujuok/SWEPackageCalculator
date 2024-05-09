package control;

import data.Packet;
import data.Utils.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    private final Calculator calculator = new Calculator();

    @Test
    public void whenWeightExceedsLimit_thenThrowException() {
        Packet packet = new Packet(1200, 600, 600, 32000);

        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet),
                "Expected calcShippingCosts to throw, but it didn't");
    }

    @Test
    public void whenDimensionsExceedLimits_thenThrowException() {
        Packet packet = new Packet(1300, 700, 700, 10000);

        assertThrows(IllegalArgumentException.class, () -> calculator.calcShippingCosts(packet),
                "Expected calcShippingCosts to throw, but it didn't");
    }

    @Test
    public void whenNegativeDimensionsProvided_thenAssert() {
        Packet packet = new Packet(-1, 600, 600, 5000);

        AssertionError assertionError = assertThrows(AssertionError.class, () -> calculator.calcShippingCosts(packet),
                "Expected calcShippingCosts to throw an AssertionError due to negative length, but it didn't");

        assertTrue(assertionError.getMessage().contains("Length must be positive"), "Assertion message mismatch.");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/package_costs.csv", numLinesToSkip = 1)
    void testPostageCalculation(int length, int width, int height, int weight, double expectedPostage) {
        Packet packet = new Packet(length, width, height, weight);
        double actualPostage = calculator.calcShippingCosts(packet);
        assertEquals(expectedPostage, actualPostage,
                () -> "Failed for: " + length + "x" + width + "x" + height + ", " + weight + "kg");
    }
}