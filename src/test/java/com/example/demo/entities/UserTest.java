package com.example.demo.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User("test@example.com", "pass123");
        assertEquals("test@example.com", user.getUemail());
        assertEquals("pass123", user.getUpassword());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setU_id(1);
        user.setUname("John");
        user.setUemail("john@example.com");
        user.setUpassword("12345");
        user.setUnumber(9876543210L);
        assertEquals(1, user.getU_id());
        assertEquals("John", user.getUname());
        assertEquals("john@example.com", user.getUemail());
        assertEquals("12345", user.getUpassword());
        assertEquals(9876543210L, user.getUnumber());
    }

    @Test
    void testOrdersAssignment() {
        User user = new User();
        List<Orders> ordersList = new ArrayList<>();
        Orders order = new Orders();
        ordersList.add(order);
        user.setOrders(ordersList);
        assertEquals(1, user.getOrders().size());
        assertEquals(order, user.getOrders().get(0));
    }

    @ParameterizedTest
    @CsvSource({
        "user1@example.com, pass1",
        "user2@example.com, pass2",
        "user3@example.com, pass3"
    })
    void testParameterizedConstructorWithMultipleValues(String email, String password) {
        User user = new User(email, password);
        assertEquals(email, user.getUemail());
        assertEquals(password, user.getUpassword());
    }

    @Test
    void testToStringContainsFields() {
        User user = new User();
        user.setU_id(5);
        user.setUname("Alice");
        user.setUemail("alice@example.com");
        user.setUpassword("pwd");
        user.setUnumber(1234567890L);
        String result = user.toString();
        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("alice@example.com"));
    }
}
