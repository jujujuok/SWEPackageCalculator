package control;

import data.Packet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    private final Calculator calculator = new Calculator();



    @Test
    public void testRandomPackets() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int length = random.nextInt(1200) + 1;
            int width = random.nextInt(600) + 1;
            int height = random.nextInt(600) + 1;
            int weight = random.nextInt(31500) + 1;
            Packet packet = new Packet(length, width, height, weight);
            double cost = calculator.calcShippingCosts(packet, false, 0);

            double expectedCost = testCalcShippingCosts(length, width, height, weight);

            if (Math.abs(cost - expectedCost) > 0.01) {
                System.out.println("Failed packet: " + packet.toString());
                System.out.println("Expected cost: " + expectedCost + ", but was: " + cost);
            }
            assertEquals(expectedCost, cost, 0.01);
        }
    }

    private double testCalcShippingCosts(int length, int width, int height, int weight) {
        System.out.println("Calculating total costs");

        List<Double> shippingCosts = new ArrayList<>(); // Default: DHL
        shippingCosts.add(3.89);
        shippingCosts.add(4.39);
        shippingCosts.add(5.99);
        shippingCosts.add(7.99);
        shippingCosts.add(14.99);

        double combinedDimensions = length + 2*width + 2*height;

        double cost = 0.0;

        // Check if packet dimensions and weight are within limits
        if (width < 0 || height < 0 || weight < 0 || length < 0) {
            throw new IllegalArgumentException("Package dimensions or weight are invalid");
        }

        // Determine size and corresponding cost
        if (length <= 300 && width <= 300 && height <= 150 && weight <= 1000) {
            cost = shippingCosts.get(0);
        } else if (length <= 600 && width <= 300 && height <= 150 && weight <= 2000) {
            cost = shippingCosts.get(1);
        } else if (length <= 1200 && width <= 600 && height <= 600) {
            // Check combined dimensions and weight for large packet
            if (combinedDimensions <= 3000 && weight <= 10000) {
                cost = weight <= 5000 ? shippingCosts.get(2) : shippingCosts.get(3);
            } else if (weight <= 31500) {
                cost = shippingCosts.get(4);
            } else {
                throw new IllegalArgumentException("Package weight exceeds the maximum limit");
            }
        } else {
            throw new IllegalArgumentException("Package dimensions exceed the maximum limits");
        }

        return cost;
    }

}