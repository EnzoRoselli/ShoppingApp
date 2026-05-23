package com.shoppingapp.unit;

import com.shoppingapp.model.OrderSummary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSummaryTest {

    @Test
    void gettersReturnEveryField() {
        OrderSummary s = new OrderSummary(100.00, 6.00, 10.00, 116.00);
        assertEquals(100.00, s.getSubtotal());
        assertEquals(6.00, s.getTax());
        assertEquals(10.00, s.getShipping());
        assertEquals(116.00, s.getTotal());
    }

    @Test
    void toStringShowsTheWholeBreakdown() {
        OrderSummary s = new OrderSummary(100.00, 6.00, 10.00, 116.00);
        assertEquals("Subtotal: $100.00 | Tax: $6.00 | Shipping: $10.00 | Total: $116.00", s.toString());
    }
}
