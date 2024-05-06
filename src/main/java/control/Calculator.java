package control;

import data.Packet;

public class Calculator {

    public double calcShippingCosts(Packet pack) {
        // Defensive programming: assert that packet dimensions and weight are positive.
        assert pack.length > 0 : "Length must be positive";
        assert pack.width > 0 : "Width must be positive";
        assert pack.height > 0 : "Height must be positive";
        assert pack.weight > 0 : "Weight must be positive";

        double shippingCosts = 0.0;
        int girth = pack.length + (2 * pack.width) + (2 * pack.height); // Calculate the girth (Gurtmaß)

        // Applying rules based on the packet size and weight
        if (pack.length <= 300 && pack.width <= 300 && pack.height <= 150 && pack.weight <= 1000) {
            shippingCosts = 3.89;
        } else if (pack.length <= 600 && pack.width <= 300 && pack.height <= 150 && pack.weight <= 2000) {
            shippingCosts = 4.39;
        } else if (pack.length <= 1200 && pack.width <= 600 && pack.height <= 600 && girth <= 300) {
            if (pack.weight <= 5000) {
                shippingCosts = 5.99;
            } else if (pack.weight <= 10000) {
                shippingCosts = 7.99;
            } else if (pack.weight <= 31500) {
                shippingCosts = 14.99;
            } else {
                assert false : "Package weight exceeds the maximum limit";
            }
        } else {
            assert false : "Package dimensions exceed the maximum limits or invalid";
        }

        return shippingCosts;
    }
}
