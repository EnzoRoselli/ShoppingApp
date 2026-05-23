package com.shoppingapp.integration;

import com.shoppingapp.model.Item;
import com.shoppingapp.model.OrderSummary;
import com.shoppingapp.model.ShippingOption;
import com.shoppingapp.model.ShoppingCart;
import com.shoppingapp.service.ItemCatalog;
import com.shoppingapp.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartPricingIntegrationTest {

    private ItemCatalog catalog;
    private ShoppingCart cart;
    private PricingService pricing;

    @BeforeEach
    void setup() {
        catalog = new ItemCatalog();
        cart = new ShoppingCart();
        pricing = new PricingService();
    }

    @Test
    void smallOrderInIllinoisGetsTaxAndStandardShipping() {
        Item book = catalog.findItem("Book");
        cart.addItem(book, 2);

        OrderSummary s = pricing.summarize(cart.getRawTotal(), "IL", ShippingOption.STANDARD);

        assertEquals(24.00, s.getSubtotal());
        assertEquals(1.44, s.getTax(), 0.0001);
        assertEquals(10.00, s.getShipping());
        assertEquals(35.44, s.getTotal(), 0.0001);
    }

    @Test
    void bigOrderGetsFreeStandardShipping() {
        cart.addItem(catalog.findItem("Laptop"), 1);

        OrderSummary s = pricing.summarize(cart.getRawTotal(), "WA", ShippingOption.STANDARD);

        assertEquals(800.00, s.getSubtotal());
        assertEquals(0.0, s.getTax());
        assertEquals(0.0, s.getShipping());
        assertEquals(800.00, s.getTotal());
    }

    @Test
    void editingQuantityFlowsThroughToTheTotal() {
        Item phone = catalog.findItem("Phone");
        cart.addItem(phone, 1);
        cart.editQuantity("Phone", 2);

        OrderSummary s = pricing.summarize(cart.getRawTotal(), "NY", ShippingOption.NEXT_DAY);

        assertEquals(1199.98, s.getSubtotal(), 0.0001);
        assertEquals(71.9988, s.getTax(), 0.0001);
        assertEquals(25.00, s.getShipping());
    }

    @Test
    void mixedCartTotalsEverythingTogether() {
        cart.addItem(catalog.findItem("Charger"), 2);
        cart.addItem(catalog.findItem("Shirt"), 1);

        OrderSummary s = pricing.summarize(cart.getRawTotal(), "WA", ShippingOption.STANDARD);

        assertEquals(65.48, s.getSubtotal(), 0.0001);
        assertEquals(0.0, s.getShipping());
        assertEquals(65.48, s.getTotal(), 0.0001);
    }
}
