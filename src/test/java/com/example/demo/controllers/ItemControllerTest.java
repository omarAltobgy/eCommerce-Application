package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemByNameSuccess() {
        Item item = new Item();
        item.setName("testItem");
        item.setDescription("testItemDescription");
        item.setPrice(BigDecimal.valueOf(1.0));

        List<Item> itemsList = new ArrayList<>();
        itemsList.add(item);

        when(itemRepository.findByName("testItem")).thenReturn(itemsList);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(itemsList, response.getBody());
    }

    @Test
    public void getItemByNameFailure() {
        when(itemRepository.findByName("testItem")).thenReturn(null);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
