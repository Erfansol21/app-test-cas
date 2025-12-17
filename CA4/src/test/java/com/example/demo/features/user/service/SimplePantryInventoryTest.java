package com.example.demo.features.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.features.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimplePantryInventoryTest {

    private SimplePantryInventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new SimplePantryInventory();
    }

    private Product createProduct(int id, String name, int stock, int servingSize) {
        Product p = new Product();
        p.setPid(id);
        p.setPname(name);
        p.setInitialStock(stock);
        p.setDefaultServingSize(servingSize);
        return p;
    }

    @Test
    void testHasIngredientsAvailable() {
        Product p = createProduct(1, "Apple", 10, 1);
        assertTrue(inventory.hasIngredients(p));
    }

    @Test
    void testReserveReducesStock() {
        Product p = createProduct(1, "Apple", 5, 1);
        assertTrue(inventory.hasIngredients(p));
        inventory.reserve(p);
        assertTrue(inventory.hasIngredients(p));
        inventory.reserve(p);
        inventory.reserve(p);
        inventory.reserve(p);
        inventory.reserve(p);
        assertFalse(inventory.hasIngredients(p));
    }

    @Test
    void testNullProduct() {
        assertFalse(inventory.hasIngredients(null));
        inventory.reserve(null);
    }
}
