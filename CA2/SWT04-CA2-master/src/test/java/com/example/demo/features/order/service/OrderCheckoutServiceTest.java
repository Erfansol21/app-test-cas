package com.example.demo.features.order.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.features.user.model.User;
import java.util.ArrayList;
import java.util.List;

public class OrderCheckoutServiceTest {

    static void verify(boolean condition, String message) {
        if (!condition) fail("Verification failed: " + message);
    }

    static void verifyNoInteractions(InteractionTracker tracker, String dependencyName) {
        verify(!tracker.called(), dependencyName + " should have no interactions");
    }

    interface InteractionTracker {
        boolean called();
    }

    static class StubPaymentGateway implements PaymentGateway, InteractionTracker {
        private final boolean acceptPayment;
        private boolean called = false;

        public StubPaymentGateway(boolean acceptPayment) {
            this.acceptPayment = acceptPayment;
        }

        @Override
        public boolean charge(User user, double amount) {
            called = true;
            return acceptPayment;
        }

        @Override
        public boolean called() {
            return called;
        }
    }

    static class StubConfirmationSender implements ConfirmationSender, InteractionTracker {
        public boolean confirmationSent = false;
        private boolean called = false;

        @Override
        public void sendConfirmation(User user, double amount) {
            called = true;
            confirmationSent = true;
        }

        @Override
        public boolean called() {
            return called;
        }
    }

    static class StubOrderServices extends OrderServices implements InteractionTracker {
        private final double total;
        private boolean called = false;

        public StubOrderServices(double total) {
            this.total = total;
        }

        @Override
        public double calculateTotalForUser(User user) {
            called = true;
            return total;
        }

        @Override
        public boolean called() {
            return called;
        }
    }

    // section 4 classes

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

    static class StubOrderServices4 extends OrderServices {
        double totalToReturn;
        User capturedUser;
        StubOrderServices4(double totalToReturn) { this.totalToReturn = totalToReturn; }
        @Override
        public double calculateTotalForUser(User user) {
            capturedUser = user;
            return totalToReturn;
        }
    }

    static class StubPaymentGateway4 implements PaymentGateway {
        List<Double> charges = new ArrayList<>();
        List<Boolean> responses;
        int callIndex = 0;
        InOrderTracker tracker;
        ArgumentCaptorDouble captor;

        StubPaymentGateway4(List<Boolean> responses, InOrderTracker tracker, ArgumentCaptorDouble captor) {
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

    static class StubConfirmationSender4 implements ConfirmationSender {
        List<Double> confirmedAmounts = new ArrayList<>();
        List<User> confirmedUsers = new ArrayList<>();
        InOrderTracker tracker;

        StubConfirmationSender4(InOrderTracker tracker) { this.tracker = tracker; }

        @Override
        public void sendConfirmation(User user, double amount) {
            tracker.record("confirmation");
            confirmedUsers.add(user);
            confirmedAmounts.add(amount);
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

        verify(orderServices.called(), "OrderServices must be called");
        verify(paymentGateway.called(), "PaymentGateway must be called");
        verify(confirmationSender.called(), "ConfirmationSender must be called");
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

        verify(orderServices.called(), "OrderServices must be called");
        verify(paymentGateway.called(), "PaymentGateway must be called");
        verifyNoInteractions(confirmationSender, "ConfirmationSender");
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

        verify(orderServices.called(), "OrderServices must be called");
        verifyNoInteractions(paymentGateway, "PaymentGateway");
        verifyNoInteractions(confirmationSender, "ConfirmationSender");
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
        assertFalse(confirmationSender.called());

        verifyNoInteractions(orderServices, "OrderServices");
        verifyNoInteractions(paymentGateway, "PaymentGateway");
        verifyNoInteractions(confirmationSender, "ConfirmationSender");
    }

    // section 4 tests

    @Test
    void testCheckout_SuccessWithCaptorAndInOrder() {
        User user = new User();
        user.setUemail("user@example.com");

        InOrderTracker tracker = new InOrderTracker();
        ArgumentCaptorDouble captor = new ArgumentCaptorDouble();

        StubOrderServices4 orderServices = new StubOrderServices4(120.0);
        StubPaymentGateway4 paymentGateway = new StubPaymentGateway4(List.of(true), tracker, captor);
        StubConfirmationSender4 confirmationSender = new StubConfirmationSender4(tracker);

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

        StubOrderServices4 orderServices = new StubOrderServices4(200.0);
        StubPaymentGateway4 paymentGateway = new StubPaymentGateway4(List.of(false, true), tracker, captor);
        StubConfirmationSender4 confirmationSender = new StubConfirmationSender4(tracker);

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

        StubOrderServices4 orderServices = new StubOrderServices4(150.0);
        StubPaymentGateway4 paymentGateway = new StubPaymentGateway4(List.of(false), tracker, captor);
        StubConfirmationSender4 confirmationSender = new StubConfirmationSender4(tracker);

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

        StubOrderServices4 orderServices = new StubOrderServices4(0.0);
        StubPaymentGateway4 paymentGateway = new StubPaymentGateway4(List.of(true), tracker, captor);
        StubConfirmationSender4 confirmationSender = new StubConfirmationSender4(tracker);

        OrderCheckoutService checkoutService = new OrderCheckoutService(orderServices, paymentGateway, confirmationSender);

        CheckoutResult result = checkoutService.checkout(user);
        assertFalse(result.isSuccessful());
        assertTrue(captor.capturedAmounts.isEmpty());
        assertTrue(confirmationSender.confirmedAmounts.isEmpty());
        tracker.verify();
    }
}
