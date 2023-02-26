package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartSuccess() {
        User user = new User();
        Item item = new Item();
        Cart cart = new Cart();
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        user.setUsername("testUser");
        user.setCart(cart);

        item.setName("testItem");
        item.setDescription("testItemDescription");
        item.setPrice(BigDecimal.valueOf(1.0));

        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(item));

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(1.0), response.getBody().getTotal());
        assertEquals("testItem", response.getBody().getItems().get(0).getName());
    }

    @Test
    public void addToCartFailure() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartSuccess() {
        User user = new User();
        Item item1 = new Item();
        Item item2 = new Item();
        Cart cart = new Cart();
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        user.setUsername("testUser");
        user.setCart(cart);

        item1.setName("testItem1");
        item1.setDescription("testItemDescription1");
        item1.setPrice(BigDecimal.valueOf(1.0));

        item2.setName("testItem2");
        item2.setDescription("testItemDescription2");
        item2.setPrice(BigDecimal.valueOf(2.0));

        cart.addItem(item1);
        cart.addItem(item2);

        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(item1));

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(2.0), response.getBody().getTotal());
        assertEquals("testItem2", response.getBody().getItems().get(0).getName());
    }

    @Test
    public void removeFromCartFailure() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(null);

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
