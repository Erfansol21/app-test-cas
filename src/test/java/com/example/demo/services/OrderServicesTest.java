package com.example.demo.services;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import com.example.demo.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServicesTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServices orderServices;

    private Orders order;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setU_id(1);
        user.setUname("John");
        order = new Orders();
        order.setoId(1);
        order.setoName("Pizza");
        order.setUser(user);
    }

    @Test
    @DisplayName("getOrders should return all orders")
    void testGetOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        List<Orders> result = orderServices.getOrders();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getoName()).isEqualTo("Pizza");
    }

    @Test
    @DisplayName("saveOrder should call save on repository")
    void testSaveOrder() {
        orderServices.saveOrder(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("updateOrder should set id and save order")
    void testUpdateOrder() {
        Orders updated = new Orders();
        updated.setoName("Burger");
        orderServices.updateOrder(2, updated);
        assertThat(updated.getoId()).isEqualTo(2);
        verify(orderRepository, times(1)).save(updated);
    }

    @Test
    @DisplayName("deleteOrder should call deleteById on repository")
    void testDeleteOrder() {
        orderServices.deleteOrder(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("getOrdersForUser should call repository with correct user")
    void testGetOrdersForUser() {
        when(orderRepository.findOrdersByUser(user)).thenReturn(Arrays.asList(order));
        List<Orders> result = orderServices.getOrdersForUser(user);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getUname()).isEqualTo("John");
        verify(orderRepository, times(1)).findOrdersByUser(user);
    }
}
