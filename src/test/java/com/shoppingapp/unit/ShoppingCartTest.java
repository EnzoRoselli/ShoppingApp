package com.shoppingapp.unit;

import com.shoppingapp.model.CartItem;
import com.shoppingapp.model.Item;
import com.shoppingapp.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;
    private final Item book = new Item("Book", 12.00);
    private final Item laptop = new Item("Laptop", 800.00);

    @BeforeEach
    void setup() {
        cart = new ShoppingCart();
    }

    @Test
    void newCartIsEmpty() {
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getItemCount());
        assertEquals(0.0, cart.getRawTotal());
    }

    @Test
    void addingItemReturnsRunningCount() {
        assertEquals(2, cart.addItem(book, 2));
        assertFalse(cart.isEmpty());
    }

    @Test
    void addingSameItemAgainStacksQuantity() {
        cart.addItem(book, 2);
        int count = cart.addItem(book, 3);
        assertEquals(5, count);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void addingDifferentItemsKeepsThemSeparate() {
        cart.addItem(book, 2);
        cart.addItem(laptop, 1);
        assertEquals(2, cart.getItems().size());
        assertEquals(3, cart.getItemCount());
    }

    @Test
    void rawTotalSumsEveryLine() {
        cart.addItem(book, 2);
        cart.addItem(laptop, 1);
        assertEquals(824.00, cart.getRawTotal());
    }

    @Test
    void removeExistingItem() {
        cart.addItem(book, 2);
        cart.removeItem("book");
        assertTrue(cart.isEmpty());
    }

    @Test
    void removeMissingItemThrows() {
        assertThrows(IllegalArgumentException.class, () -> cart.removeItem("Book"));
    }

    @Test
    void editQuantityOfExistingItem() {
        cart.addItem(book, 2);
        cart.editQuantity("BOOK", 7);
        assertEquals(7, cart.getItemCount());
    }

    @Test
    void editMissingItemThrows() {
        cart.addItem(book, 2);
        assertThrows(IllegalArgumentException.class, () -> cart.editQuantity("Laptop", 3));
    }

    @Test
    void getItemsCannotBeModified() {
        cart.addItem(book, 1);
        assertThrows(UnsupportedOperationException.class,
                () -> cart.getItems().add(new CartItem(laptop, 1)));
    }
}
