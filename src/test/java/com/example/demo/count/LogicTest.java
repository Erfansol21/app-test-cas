package com.example.demo.count;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogicTest {

    @Test
    void testCountTotalReturnsCorrectValue() {
        double price = 10.0;
        int quantity = 3;
        double expected = 30.0;

        double result = Logic.countTotal(price, quantity);

        assertEquals(expected, result, 0.0001, "Should correctly multiply price by quantity");
    }

    @Test
    void testCountTotalWithZeroQuantity() {
        double result = Logic.countTotal(10.0, 0);
        assertEquals(0.0, result, 0.0001, "Total should be zero when quantity is zero");
    }

    @Test
    void testCountTotalWithNegativeValues() {
        double result = Logic.countTotal(-5.0, 2);
        assertEquals(-10.0, result, 0.0001, "Should handle negative price correctly");
    }
}
