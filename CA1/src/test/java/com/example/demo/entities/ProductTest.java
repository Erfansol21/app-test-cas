package com.example.demo.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void testSettersAndGetters() {
        Product product = new Product();
        product.setPid(1);
        product.setPname("Tea");
        product.setPprice(10.5);
        product.setPdescription("Green tea");
        assertEquals(1, product.getPid());
        assertEquals("Tea", product.getPname());
        assertEquals(10.5, product.getPprice());
        assertEquals("Green tea", product.getPdescription());
    }

    @ParameterizedTest
    @CsvSource({
        "Coffee, 5.0, Hot drink",
        "Pizza, 12.99, Italian food",
        "Burger, 8.5, Fast food"
    })
    void testMultipleProducts(String name, double price, String description) {
        Product product = new Product();
        product.setPname(name);
        product.setPprice(price);
        product.setPdescription(description);
        assertEquals(name, product.getPname());
        assertEquals(price, product.getPprice());
        assertEquals(description, product.getPdescription());
    }

    @Test
    void testToStringContainsFields() {
        Product product = new Product();
        product.setPid(3);
        product.setPname("Juice");
        product.setPprice(4.25);
        product.setPdescription("Fresh orange juice");
        String result = product.toString();
        assertTrue(result.contains("Juice"));
        assertTrue(result.contains("4.25"));
        assertTrue(result.contains("Fresh orange juice"));
    }
}
