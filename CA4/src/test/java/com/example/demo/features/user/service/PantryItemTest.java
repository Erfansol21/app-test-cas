package com.example.demo.features.user.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PantryItemTest {

    @Test
    void testCanFulfill() {
        PantryItem item = new PantryItem(1, "Apple", 5, 1);
        assertTrue(item.canFulfill(3));
        assertFalse(item.canFulfill(6));
    }

    @Test
    void testReserveReducesAvailable() {
        PantryItem item = new PantryItem(1, "Apple", 5, 1);
        item.reserve(2);
        assertEquals(3, item.getAvailableServings());
        item.reserve(5);
        assertEquals(0, item.getAvailableServings());
    }

    @Test
    void testReserveNegativeOrZero() {
        PantryItem item = new PantryItem(1, "Apple", 5, 1);
        item.reserve(0);
        assertEquals(5, item.getAvailableServings());
        item.reserve(-3);
        assertEquals(5, item.getAvailableServings());
    }

    @Test
    void testConstructorBounds() {
        PantryItem item = new PantryItem(1, "Apple", -5, 0);
        assertEquals(0, item.getAvailableServings());
        assertEquals(1, item.getMinimumReserve());
    }

    @Test
    void testGetters() {
        PantryItem item = new PantryItem(1, "Apple", 5, 2);
        assertEquals(1, item.getProductId());
        assertEquals("Apple", item.getProductName());
        assertEquals(5, item.getAvailableServings());
        assertEquals(2, item.getMinimumReserve());
    }
}
