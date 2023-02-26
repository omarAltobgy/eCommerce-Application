package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submitOrderSuccess() {
        User user = new User();
        Cart cart = new Cart();
        Item item = new Item();

        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(1.0));
        item.setDescription("testItemDescription");

        cart.addItem(item);
        cart.setUser(user);

        user.setUsername("testUser");
        user.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertEquals(cart.getItems(), userOrder.getItems());
        assertEquals(user, userOrder.getUser());
    }

    @Test
    public void submitOrderFailure() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
