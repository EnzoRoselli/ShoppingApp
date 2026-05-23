package com.shoppingapp.unit;

import com.shoppingapp.model.OrderSummary;
import com.shoppingapp.model.ShippingOption;
import com.shoppingapp.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    private PricingService pricing;

    @BeforeEach
    void setup() {
        pricing = new PricingService();
    }

    @Test
    void illinoisIsTaxed() {
        assertEquals(0.06, pricing.getTaxRate("IL"));
    }

    @Test
    void californiaIsTaxed() {
        assertEquals(0.06, pricing.getTaxRate("CA"));
    }

    @Test
    void newYorkIsTaxed() {
        assertEquals(0.06, pricing.getTaxRate("NY"));
    }

    @Test
    void lowercaseStateStillTaxed() {
        assertEquals(0.06, pricing.getTaxRate("il"));
    }

    @Test
    void otherStateHasNoTax() {
        assertEquals(0.0, pricing.getTaxRate("WA"));
    }

    @Test
    void nextDayShippingIsTwentyFive() {
        assertEquals(25.00, pricing.calculateShipping(ShippingOption.NEXT_DAY, 5.00));
    }

    @Test
    void nextDayNeverFreeEvenWhenExpensive() {
        assertEquals(25.00, pricing.calculateShipping(ShippingOption.NEXT_DAY, 5000.00));
    }

    @Test
    void standardShippingIsTenUnderFifty() {
        assertEquals(10.00, pricing.calculateShipping(ShippingOption.STANDARD, 20.00));
    }

    @Test
    void standardShippingStillTenAtExactlyFifty() {
        assertEquals(10.00, pricing.calculateShipping(ShippingOption.STANDARD, 50.00));
    }

    @Test
    void standardShippingFreeJustOverFifty() {
        assertEquals(0.00, pricing.calculateShipping(ShippingOption.STANDARD, 50.01));
    }

    @Test
    void calculateTotalAddsTaxAndShipping() {
        assertEquals(106.00, pricing.calculateTotal(100.00, "IL", ShippingOption.STANDARD));
    }

    @Test
    void calculateTotalCountsEveryTermWhenNoneAreZero() {
        assertEquals(31.20, pricing.calculateTotal(20.00, "IL", ShippingOption.STANDARD), 0.0001);
    }

    @Test
    void validateAcceptsOneDollar() {
        assertDoesNotThrow(() -> pricing.validatePurchaseAmount(1.00));
    }

    @Test
    void validateRejectsJustUnderOne() {
        assertThrows(IllegalStateException.class, () -> pricing.validatePurchaseAmount(0.99));
    }

    @Test
    void validateAcceptsMax() {
        assertDoesNotThrow(() -> pricing.validatePurchaseAmount(99999.99));
    }

    @Test
    void validateRejectsOverMax() {
        assertThrows(IllegalStateException.class, () -> pricing.validatePurchaseAmount(100000.00));
    }

    @Test
    void summarizeBuildsFullBreakdown() {
        OrderSummary s = pricing.summarize(100.00, "IL", ShippingOption.NEXT_DAY);
        assertEquals(100.00, s.getSubtotal());
        assertEquals(6.00, s.getTax());
        assertEquals(25.00, s.getShipping());
        assertEquals(131.00, s.getTotal());
    }
}
