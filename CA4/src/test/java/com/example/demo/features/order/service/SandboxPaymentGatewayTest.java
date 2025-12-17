package com.example.demo.features.order.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.features.user.model.User;

class SandboxPaymentGatewayTest {

    SandboxPaymentGateway gateway = new SandboxPaymentGateway();

    @Test
    void nullUserRejected() {
        assertFalse(gateway.charge(null, 50));
    }

    @Test
    void nonPositiveAmountRejected() {
        User user = validUser();
        assertFalse(gateway.charge(user, 0));
    }

    @Test
    void tooManyDecimalsRejected() {
        User user = validUser();
        assertFalse(gateway.charge(user, 10.123));
    }

    @Test
    void amountAboveLimitRejected() {
        User user = validUser();
        assertFalse(gateway.charge(user, 1500));
    }

    @Test
    void invalidEmailRejected() {
        User user = validUser();
        user.setUemail("bad-email");
        assertFalse(gateway.charge(user, 50));
    }

    @Test
    void blacklistedDomainRejected() {
        User user = validUser();
        user.setUemail("test@fraud.com");
        assertFalse(gateway.charge(user, 50));
    }

    @Test
    void highRiskRejected() {
        User user = validUser();
        user.setUname("");
        user.setUnumber(null);
        user.setUemail("x@evil.ru");
        assertFalse(gateway.charge(user, 900));
    }

    @Test
    void lowRiskAccepted() {
        User user = validUser();
        assertTrue(gateway.charge(user, 50));
    }

    private User validUser() {
        User user = new User();
        user.setUname("John");
        user.setUemail("john@example.com");
        user.setUnumber(123456789L);
        return user;
    }
}
