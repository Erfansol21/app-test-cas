package com.example.demo.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class OrdersTest {

    @Test
    void testSettersAndGetters() {
        Orders order = new Orders();
        order.setoId(1);
        order.setoName("Pizza");
        order.setoPrice(12.5);
        order.setoQuantity(2);
        order.setOrderDate(new Date());
        order.setTotalAmmout(25.0);
        User user = new User();
        order.setUser(user);
        assertEquals(1, order.getoId());
        assertEquals("Pizza", order.getoName());
        assertEquals(12.5, order.getoPrice());
        assertEquals(2, order.getoQuantity());
        assertNotNull(order.getOrderDate());
        assertEquals(25.0, order.getTotalAmmout());
        assertEquals(user, order.getUser());
    }

    @ParameterizedTest
    @CsvSource({
        "Burger, 5.0, 2, 10.0",
        "Fries, 3.5, 3, 10.5",
        "Juice, 4.0, 1, 4.0"
    })
    void testOrderValues(String name, double price, int quantity, double total) {
        Orders order = new Orders();
        order.setoName(name);
        order.setoPrice(price);
        order.setoQuantity(quantity);
        order.setTotalAmmout(total);
        assertEquals(name, order.getoName());
        assertEquals(price, order.getoPrice());
        assertEquals(quantity, order.getoQuantity());
        assertEquals(total, order.getTotalAmmout());
    }

    @Test
    void testToStringContainsFields() {
        Orders order = new Orders();
        order.setoId(10);
        order.setoName("Tea");
        order.setoPrice(2.5);
        order.setoQuantity(5);
        order.setOrderDate(new Date());
        order.setTotalAmmout(12.5);
        String result = order.toString();
        assertTrue(result.contains("Tea"));
        assertTrue(result.contains("2.5"));
        assertTrue(result.contains("12.5"));
    }
}
