package com.example.demo.features.order.service;

import com.example.demo.features.order.model.Orders;
import com.example.demo.features.order.repository.OrderRepository;
import com.example.demo.features.user.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServicesTest {

    @Test
    void testGetAllOrders() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        when(repo.findAll()).thenReturn(Arrays.asList(new Orders(), new Orders()));

        List<Orders> result = service.getOrders();

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void testSaveOrder() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        Orders order = new Orders();

        service.saveOrder(order);

        verify(repo).save(order);
    }

    @Test
    void testDeleteOrder() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        service.deleteOrder(5);

        verify(repo).deleteById(5);
    }

    @Test
    void testUpdateOrder() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        Orders existing = new Orders();
        existing.setoId(20);

        Orders updated = new Orders();

        when(repo.findById(20)).thenReturn(Optional.of(existing));

        service.updateOrder(20, updated);

        InOrder order = inOrder(repo);
        order.verify(repo).save(updated);
    }

    @Test
    void testGetOrdersForUser() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        User user = new User();
        user.setU_id(1);

        Orders o1 = new Orders();
        Orders o2 = new Orders();

        when(repo.findOrdersByUser(user)).thenReturn(Arrays.asList(o1, o2));

        List<Orders> result = service.getOrdersForUser(user);

        assertEquals(2, result.size());
        verify(repo).findOrdersByUser(user);
    }

    @Test
    void testCalculateTotalForUser() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        User user = new User();
        user.setU_id(1);

        Orders o1 = new Orders();
        o1.setTotalAmmout(50);

        Orders o2 = new Orders();
        o2.setoPrice(10);
        o2.setoQuantity(2);
        o2.setTotalAmmout(0);

        when(repo.findOrdersByUser(user)).thenReturn(Arrays.asList(o1, o2));

        double total = service.calculateTotalForUser(user);

        assertEquals(70, total);
    }

    @Test
    void testCalculateTotalForUser_NoOrders() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderServices service = new OrderServices();
        ReflectionTestUtils.setField(service, "orderRepository", repo);

        User user = new User();
        user.setU_id(1);

        when(repo.findOrdersByUser(user)).thenReturn(null);

        double result = service.calculateTotalForUser(user);

        assertEquals(0.0, result);
    }
}
