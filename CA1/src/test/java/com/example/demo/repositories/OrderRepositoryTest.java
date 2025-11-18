package com.example.demo.repositories;

import com.example.demo.entities.Orders;
import com.example.demo.entities.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findOrdersByUser should return orders belonging to the given user")
    void testFindOrdersByUser() {
        // Arrange: create a user
        User user = new User();
        user.setUname("John Doe");
        user.setUemail("john@example.com");
        user.setUpassword("password");
        user.setUnumber(1234567890L);
        userRepository.save(user);

        // Create orders for that user
        Orders order1 = new Orders();
        order1.setoName("Pizza");
        order1.setoPrice(12.5);
        order1.setoQuantity(1);
        order1.setTotalAmmout(12.5);
        order1.setOrderDate(new Date());
        order1.setUser(user);
        orderRepository.save(order1);

        Orders order2 = new Orders();
        order2.setoName("Burger");
        order2.setoPrice(8.0);
        order2.setoQuantity(2);
        order2.setTotalAmmout(16.0);
        order2.setOrderDate(new Date());
        order2.setUser(user);
        orderRepository.save(order2);

        // Act: call repository method
        List<Orders> foundOrders = orderRepository.findOrdersByUser(user);

        // Assert
        assertThat(foundOrders).isNotNull();
        assertThat(foundOrders.size()).isEqualTo(2);
        assertThat(foundOrders)
                .extracting(Orders::getoName)
                .containsExactlyInAnyOrder("Pizza", "Burger");
    }
}
