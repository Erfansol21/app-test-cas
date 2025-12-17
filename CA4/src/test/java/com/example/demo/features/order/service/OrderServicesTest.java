package com.example.demo.features.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.features.order.model.Orders;
import com.example.demo.features.order.repository.OrderRepository;
import com.example.demo.features.user.model.User;

@ExtendWith(MockitoExtension.class)
class OrderServicesTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServices service;

    @Test
    void nullOrderListReturnsZero() {
        User user = new User();
        when(orderRepository.findOrdersByUser(user)).thenReturn(null);
        assertEquals(0.0, service.calculateTotalForUser(user));
    }

    @Test
    void emptyOrderListReturnsZero() {
        User user = new User();
        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of());
        assertEquals(0.0, service.calculateTotalForUser(user));
    }

    @Test
    void validOrdersAreSummed() {
        User user = new User();
        Orders o1 = new Orders();
        o1.setoPrice(10);
        o1.setoQuantity(2);
        Orders o2 = new Orders();
        o2.setoPrice(5);
        o2.setoQuantity(3);
        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of(o1, o2));
        assertEquals(35.0, service.calculateTotalForUser(user));
    }

    @Test
    void precomputedTotalIsUsed() {
        User user = new User();
        Orders o = new Orders();
        o.setTotalAmmout(99.99);
        o.setoPrice(1);
        o.setoQuantity(1);
        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of(o));
        assertEquals(99.99, service.calculateTotalForUser(user));
    }

    @Test
    void zeroQuantityThrows() {
        User user = new User();
        Orders o = new Orders();
        o.setoPrice(10);
        o.setoQuantity(0);
        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of(o));
        assertThrows(IllegalArgumentException.class, () -> service.calculateTotalForUser(user));
    }

    @Test
    void negativePriceThrows() {
        User user = new User();
        Orders o = new Orders();
        o.setoPrice(-1);
        o.setoQuantity(1);
        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of(o));
        assertThrows(IllegalArgumentException.class, () -> service.calculateTotalForUser(user));
    }

    @Test
    void exceedingMaxTotalThrows() {
        User user = new User();
        Orders o = new Orders();
        o.setTotalAmmout(20000);
        o.setoPrice(1);
        o.setoQuantity(1);
        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of(o));
        assertThrows(IllegalArgumentException.class, () -> service.calculateTotalForUser(user));
    }
}
