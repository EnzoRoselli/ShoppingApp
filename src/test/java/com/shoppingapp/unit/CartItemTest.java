package com.shoppingapp.unit;

import com.shoppingapp.model.CartItem;
import com.shoppingapp.model.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    private final Item book = new Item("Book", 12.00);

    @Test
    void constructorKeepsItemAndQuantity() {
        CartItem line = new CartItem(book, 3);
        assertEquals(book, line.getItem());
        assertEquals(3, line.getQuantity());
    }

    @Test
    void quantityOfOneIsAllowed() {
        assertEquals(1, new CartItem(book, 1).getQuantity());
    }

    @Test
    void constructorRejectsZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(book, 0));
    }

    @Test
    void setQuantityUpdatesValue() {
        CartItem line = new CartItem(book, 1);
        line.setQuantity(5);
        assertEquals(5, line.getQuantity());
    }

    @Test
    void setQuantityRejectsZero() {
        CartItem line = new CartItem(book, 2);
        assertThrows(IllegalArgumentException.class, () -> line.setQuantity(0));
    }

    @Test
    void setQuantityAcceptsExactlyOne() {
        CartItem line = new CartItem(book, 4);
        line.setQuantity(1);
        assertEquals(1, line.getQuantity());
    }

    @Test
    void lineTotalIsPriceTimesQuantity() {
        assertEquals(36.00, new CartItem(book, 3).getLineTotal());
    }

    @Test
    void toStringShowsLineBreakdown() {
        assertEquals("Book x3 = $36.00", new CartItem(book, 3).toString());
    }
}
