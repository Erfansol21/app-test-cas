package com.example.demo.features.order.service;

import com.example.demo.features.user.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class OrderCheckoutServiceSectionFourTest {

    static class ArgumentCaptorDouble {
        List<Double> capturedAmounts = new ArrayList<>();
        void capture(double amount) { capturedAmounts.add(amount); }
        double getValue() { return capturedAmounts.get(capturedAmounts.size() - 1); }
    }

    static class InOrderTracker {
        List<String> calls = new ArrayList<>();
        void record(String call) { calls.add(call); }
        void verify(String... expectedOrder) {
            assertEquals(expectedOrder.length, calls.size());
            for (int i = 0; i < expectedOrder.length; i++) {
                assertEquals(expectedOrder[i], calls.get(i));
            }
        }
    }

    static class StubOrderServices extends OrderServices {
        double totalToReturn;
        User capturedUser;
        StubOrderServices(double totalToReturn) { this.totalToReturn = totalToReturn; }
        @Override
        public double calculateTotalForUser(User user) {
            capturedUser = user;
            return totalToReturn;
        }
    }

    static class StubPaymentGateway implements PaymentGateway {
        List<Double> charges = new ArrayList<>();
        List<Boolean> responses;
        int callIndex = 0;
        InOrderTracker tracker;
        ArgumentCaptorDouble captor;

        StubPaymentGateway(List<Boolean> responses, InOrderTracker tracker, ArgumentCaptorDouble captor) {
            this.responses = responses;
            this.tracker = tracker;
            this.captor = captor;
        }

        @Override
        public boolean charge(User user, double amount) {
            tracker.record("charge");
            captor.capture(amount);
            charges.add(amount);
            boolean response = responses.get(callIndex);
            callIndex++;
            return response;
        }
    }

    static class StubConfirmationSender implements ConfirmationSender {
        List<Double> confirmedAmounts = new ArrayList<>();
        List<User> confirmedUsers = new ArrayList<>();
        InOrderTracker tracker;

        StubConfirmationSender(InOrderTracker tracker) { this.tracker = tracker; }

        @Override
        public void sendConfirmation(User user, double amount) {
            tracker.record("confirmation");
            confirmedUsers.add(user);
            confirmedAmounts.add(amount);
        }
    }

    @Test
    void testCheckout_SuccessWithCaptorAndInOrder() {
        User user = new User();
        user.setUemail("user@example.com");

        InOrderTracker tracker = new InOrderTracker();
        ArgumentCaptorDouble captor = new ArgumentCaptorDouble();

        StubOrderServices orderServices = new StubOrderServices(120.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(List.of(true), tracker, captor);
        StubConfirmationSender confirmationSender = new StubConfirmationSender(tracker);

        OrderCheckoutService checkoutService = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = checkoutService.checkout(user);

        assertTrue(result.isSuccessful());
        assertEquals(120.0, result.getChargedAmount());
        assertEquals(120.0, captor.getValue());
        tracker.verify("charge", "confirmation");
    }

    @Test
    void testCheckout_FirstFailsSecondSucceedsWithThenReturn() {
        User user = new User();
        user.setUemail("user@example.com");

        InOrderTracker tracker = new InOrderTracker();
        ArgumentCaptorDouble captor = new ArgumentCaptorDouble();

        StubOrderServices orderServices = new StubOrderServices(200.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(List.of(false, true), tracker, captor);
        StubConfirmationSender confirmationSender = new StubConfirmationSender(tracker);

        OrderCheckoutService checkoutService = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult firstAttempt = checkoutService.checkout(user);
        assertFalse(firstAttempt.isSuccessful());

        CheckoutResult secondAttempt = checkoutService.checkout(user);
        assertTrue(secondAttempt.isSuccessful());
        assertEquals(200.0, secondAttempt.getChargedAmount());
        assertEquals(200.0, captor.getValue());

        tracker.verify("charge", "charge", "confirmation");
    }

    @Test
    void testPaymentFailure_NoConfirmation() {
        User user = new User();
        user.setUemail("fail@example.com");

        InOrderTracker tracker = new InOrderTracker();
        ArgumentCaptorDouble captor = new ArgumentCaptorDouble();

        StubOrderServices orderServices = new StubOrderServices(150.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(List.of(false), tracker, captor);
        StubConfirmationSender confirmationSender = new StubConfirmationSender(tracker);

        OrderCheckoutService checkoutService = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = checkoutService.checkout(user);
        assertFalse(result.isSuccessful());
        assertEquals(150.0, captor.getValue());
        assertTrue(confirmationSender.confirmedAmounts.isEmpty());
        tracker.verify("charge");
    }

    @Test
    void testFailureOnRecalculation_ZeroTotal() {
        User user = new User();
        user.setUemail("zero@example.com");

        InOrderTracker tracker = new InOrderTracker();
        ArgumentCaptorDouble captor = new ArgumentCaptorDouble();

        StubOrderServices orderServices = new StubOrderServices(0.0);
        StubPaymentGateway paymentGateway = new StubPaymentGateway(List.of(true), tracker, captor);
        StubConfirmationSender confirmationSender = new StubConfirmationSender(tracker);

        OrderCheckoutService checkoutService = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = checkoutService.checkout(user);
        assertFalse(result.isSuccessful());
        assertTrue(captor.capturedAmounts.isEmpty());
        assertTrue(confirmationSender.confirmedAmounts.isEmpty());
        tracker.verify();
    }
}
