package com.example.demo.features.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.example.demo.features.user.model.User;

class OrderCheckoutServiceTest {

    static class FakeOrderServices extends OrderServices {
        double total;
        @Override
        public double calculateTotalForUser(User user) {
            return total;
        }
    }

    @Test
    void nullUserFails() {
        FakeOrderServices orderServices = new FakeOrderServices();
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        ConfirmationSender confirmationSender = mock(ConfirmationSender.class);

        OrderCheckoutService service =
                new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = service.checkout(null);

        assertFalse(result.isSuccessful());
        assertEquals("User details are required", result.getMessage());
    }

    @Test
    void zeroCartTotalFails() {
        FakeOrderServices orderServices = new FakeOrderServices();
        orderServices.total = 0.0;

        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        ConfirmationSender confirmationSender = mock(ConfirmationSender.class);

        OrderCheckoutService service =
                new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = service.checkout(new User());

        assertFalse(result.isSuccessful());
        assertEquals("Cart total must be greater than zero", result.getMessage());
    }

    @Test
    void paymentDeclinedFails() {
        FakeOrderServices orderServices = new FakeOrderServices();
        orderServices.total = 100.0;

        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        ConfirmationSender confirmationSender = mock(ConfirmationSender.class);

        when(paymentGateway.charge(any(), eq(100.0))).thenReturn(false);

        OrderCheckoutService service =
                new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = service.checkout(new User());

        assertFalse(result.isSuccessful());
        assertEquals("Payment declined", result.getMessage());
    }

    @Test
    void successfulCheckout() {
        FakeOrderServices orderServices = new FakeOrderServices();
        orderServices.total = 75.5;

        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        ConfirmationSender confirmationSender = mock(ConfirmationSender.class);

        when(paymentGateway.charge(any(), eq(75.5))).thenReturn(true);

        OrderCheckoutService service =
                new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        User user = new User();
        CheckoutResult result = service.checkout(user);

        assertTrue(result.isSuccessful());
        assertEquals(75.5, result.getChargedAmount());
        verify(confirmationSender).sendConfirmation(user, 75.5);
    }
}
