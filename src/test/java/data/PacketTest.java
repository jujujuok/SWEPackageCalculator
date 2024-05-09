package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PacketTest {

    @Test
    public void whenNegativeDimension_thenAssert() {
        assertThrows(AssertionError.class, () ->
                        new Packet(-1, 600, 600, 500),
                "Expected calcShippingCosts to throw an AssertionError due to negative length, but it didn't");
    }

    @Test
    public void whenDimensionsExceedLimits_thenThrowException() {
        assertThrows(AssertionError.class, () -> new Packet(1300, 700, 700, 10000),
                "Expected calcShippingCosts to throw, but it didn't");
    }

    @Test
    public void whenWeightExceedsLimit_thenThrowException() {
        assertThrows(AssertionError.class, () -> new Packet(1200, 600, 600, 32000),
                "Expected calcShippingCosts to throw, but it didn't");
    }
}
