package com.example.demo.features.order.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.features.user.model.User;

class LoggingConfirmationSenderTest {

    LoggingConfirmationSender sender = new LoggingConfirmationSender();

    @Test
    void nullUserDoesNotThrow() {
        assertDoesNotThrow(() -> sender.sendConfirmation(null, 10));
    }

    @Test
    void nonPositiveAmountDoesNotThrow() {
        User user = new User();
        user.setUemail("a@b.com");
        assertDoesNotThrow(() -> sender.sendConfirmation(user, 0));
    }

    @Test
    void invalidEmailDoesNotThrow() {
        User user = new User();
        user.setUemail("bad");
        assertDoesNotThrow(() -> sender.sendConfirmation(user, 10));
    }

    @Test
    void validInputDoesNotThrow() {
        User user = new User();
        user.setUemail("valid@test.com");
        assertDoesNotThrow(() -> sender.sendConfirmation(user, 10));
    }
}
