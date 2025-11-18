package com.example.demo.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void testSettersAndGetters() {
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminName("Alice");
        admin.setAdminEmail("alice@example.com");
        admin.setAdminPassword("secret");
        admin.setAdminNumber("1234567890");

        assertEquals(1, admin.getAdminId());
        assertEquals("Alice", admin.getAdminName());
        assertEquals("alice@example.com", admin.getAdminEmail());
        assertEquals("secret", admin.getAdminPassword());
        assertEquals("1234567890", admin.getAdminNumber());
    }

    @ParameterizedTest
    @CsvSource({
        "Bob, bob@example.com, pass1, 9876543210",
        "Carol, carol@example.com, pass2, 1112223333",
        "Dave, dave@example.com, pass3, 4445556666"
    })
    void parameterizedAdminDataTest(String name, String email, String password, String number) {
        Admin admin = new Admin();
        admin.setAdminName(name);
        admin.setAdminEmail(email);
        admin.setAdminPassword(password);
        admin.setAdminNumber(number);

        assertEquals(name, admin.getAdminName());
        assertEquals(email, admin.getAdminEmail());
        assertEquals(password, admin.getAdminPassword());
        assertEquals(number, admin.getAdminNumber());
    }

    @Test
    void testToStringContainsFields() {
        Admin admin = new Admin();
        admin.setAdminId(10);
        admin.setAdminName("TestAdmin");
        admin.setAdminEmail("test@admin.com");
        admin.setAdminPassword("pwd");
        admin.setAdminNumber("5554443333");

        String result = admin.toString();

        assertTrue(result.contains("TestAdmin"));
        assertTrue(result.contains("test@admin.com"));
        assertTrue(result.contains("pwd"));
        assertTrue(result.contains("5554443333"));
    }
}
