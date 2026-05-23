package com.shoppingapp.unit;

import com.shoppingapp.model.Item;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void gettersReturnNameAndPrice() {
        Item book = new Item("Book", 12.00);
        assertEquals("Book", book.getName());
        assertEquals(12.00, book.getPrice());
    }

    @Test
    void sameReferenceIsEqual() {
        Item book = new Item("Book", 12.00);
        assertEquals(book, book);
    }

    @Test
    void equalsIsCaseInsensitiveOnName() {
        Item lower = new Item("book", 12.00);
        Item upper = new Item("BOOK", 999.00);
        assertEquals(lower, upper);
    }

    @Test
    void differentNamesAreNotEqual() {
        assertNotEquals(new Item("Book", 12.00), new Item("Laptop", 12.00));
    }

    @Test
    void notEqualToNull() {
        Item book = new Item("Book", 12.00);
        assertFalse(book.equals(null));
    }

    @Test
    void notEqualToOtherType() {
        Item book = new Item("Book", 12.00);
        assertFalse(book.equals("Book"));
    }

    @Test
    void hashCodeMatchesForSameNameDifferentCase() {
        assertEquals(new Item("book", 1).hashCode(), new Item("BOOK", 2).hashCode());
    }

    @Test
    void hashCodeIsBasedOnTheLowercasedName() {
        assertEquals(Objects.hash("book"), new Item("Book", 12.00).hashCode());
    }

    @Test
    void toStringShowsNameAndPrice() {
        assertEquals("Book - $12.00", new Item("Book", 12.00).toString());
    }
}
