package com.shoppingapp.unit;

import com.shoppingapp.model.ShippingOption;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShippingOptionTest {

    @Test
    void hasTheTwoShippingKinds() {
        assertEquals(2, ShippingOption.values().length);
        assertEquals(ShippingOption.STANDARD, ShippingOption.valueOf("STANDARD"));
        assertEquals(ShippingOption.NEXT_DAY, ShippingOption.valueOf("NEXT_DAY"));
    }
}
