package com.shoppingapp.unit;

import com.shoppingapp.model.Item;
import com.shoppingapp.service.ItemCatalog;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemCatalogTest {

    @Test
    void defaultCatalogHasTheSampleProducts() {
        ItemCatalog catalog = new ItemCatalog();
        assertEquals(6, catalog.getAllItems().size());
    }

    @Test
    void findIsCaseInsensitive() {
        ItemCatalog catalog = new ItemCatalog();
        assertEquals("Book", catalog.findItem("book").getName());
    }

    @Test
    void findMissingItemThrows() {
        ItemCatalog catalog = new ItemCatalog();
        assertThrows(IllegalArgumentException.class, () -> catalog.findItem("spaceship"));
    }

    @Test
    void customCatalogOnlyHasGivenItems() {
        ItemCatalog catalog = new ItemCatalog(List.of(new Item("Pen", 1.50)));
        assertEquals(1, catalog.getAllItems().size());
        assertEquals(1.50, catalog.findItem("pen").getPrice());
    }
}
