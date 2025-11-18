package com.example.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoderBeanLoads() {
        assertNotNull(passwordEncoder, "PasswordEncoder bean should be loaded by Spring");
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder, 
                   "PasswordEncoder should be an instance of BCryptPasswordEncoder");
    }
}
