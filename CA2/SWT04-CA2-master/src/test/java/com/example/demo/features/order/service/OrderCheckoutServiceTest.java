package com.example.demo.features.order.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.features.order.service.*;
import com.example.demo.features.user.model.User;

public class OrderCheckoutServiceTest {

    static class StubPaymentGateway implements PaymentGateway {
        private final boolean acceptPayment;

        public StubPaymentGateway(boolean acceptPayment) {
            this.acceptPayment = acceptPayment;
        }

        @Override
        public boolean charge(User user, double amount) {
            return acceptPayment;
        }
    }

    static class StubConfirmationSender implements ConfirmationSender {
        public boolean confirmationSent = false;

        @Override
        public void sendConfirmation(User user, double amount) {
            confirmationSent = true;
        }
    }

    static class StubOrderServices extends OrderServices {
        private final double total;

        public StubOrderServices(double total) {
            this.total = total;
        }

        @Override
        public double calculateTotalForUser(User user) {
            return total;
        }
    }

    @Test
    void testCheckoutSuccess() {
        User user = new User();
        StubOrderServices orderServices = new StubOrderServices(100.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(true);
        StubConfirmationSender confirmationSender = new StubConfirmationSender();

        OrderCheckoutService service = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);
        CheckoutResult result = service.checkout(user);

        assertTrue(result.isSuccessful());
        assertEquals(100.0, result.getChargedAmount());
        assertTrue(confirmationSender.confirmationSent);
    }

    @Test
    void testCheckoutPaymentDeclined() {
        User user = new User();
        StubOrderServices orderServices = new StubOrderServices(50.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(false);
        StubConfirmationSender confirmationSender = new StubConfirmationSender();

        OrderCheckoutService service = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);
        CheckoutResult result = service.checkout(user);

        assertFalse(result.isSuccessful());
        assertEquals("Payment declined", result.getMessage());
        assertFalse(confirmationSender.confirmationSent);
    }

    @Test
    void testCheckoutEmptyCart() {
        User user = new User();
        StubOrderServices orderServices = new StubOrderServices(0.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(true);
        StubConfirmationSender confirmationSender = new StubConfirmationSender();

        OrderCheckoutService service = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);
        CheckoutResult result = service.checkout(user);

        assertFalse(result.isSuccessful());
        assertEquals("Cart total must be greater than zero", result.getMessage());
        assertFalse(confirmationSender.confirmationSent);
    }

    @Test
    void testCheckoutNullUser() {
        StubOrderServices orderServices = new StubOrderServices(100.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(true);
        StubConfirmationSender confirmationSender = new StubConfirmationSender();

        OrderCheckoutService service = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);
        CheckoutResult result = service.checkout(null);

        assertFalse(result.isSuccessful());
        assertEquals("User details are required", result.getMessage());
        assertFalse(confirmationSender.confirmationSent);
    }
}

