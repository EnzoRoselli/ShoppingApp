package com.shoppingapp.acceptance;

import com.shoppingapp.cli.ShoppingCLI;
import com.shoppingapp.model.ShippingOption;
import com.shoppingapp.service.ItemCatalog;
import com.shoppingapp.service.PricingService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseAcceptanceTest {

    private final PricingService pricing = new PricingService();

    private String session(String script) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(captured, true, StandardCharsets.UTF_8);
        new ShoppingCLI(scanner, out, new ItemCatalog(), new PricingService()).run();
        return captured.toString(StandardCharsets.UTF_8);
    }

    @Test
    void addingAnItemTellsTheUserTheCount() {
        String out = session(String.join("\n", "A", "IL", "1", "1", "Book", "3", "7") + "\n");
        assertTrue(out.contains("added! cart has 3 item(s)"));
    }

    @Test
    void getTotalIncludesTaxAndShipping() {
        String out = session(String.join("\n", "A", "IL", "1", "1", "Book", "1", "3", "7") + "\n");
        assertTrue(out.contains("Tax: $0.72"));
        assertTrue(out.contains("Shipping: $10.00"));
        assertTrue(out.contains("Total: $22.72"));
    }

    @Test
    void checkoutReportsTransactionCompleted() {
        String out = session(String.join("\n", "A", "IL", "1", "1", "Book", "1", "6", "7") + "\n");
        assertTrue(out.contains("Transaction completed."));
    }

    @Test
    void quantityBelowOneIsRejected() {
        String out = session(String.join("\n", "A", "IL", "1", "1", "Book", "0", "7") + "\n");
        assertTrue(out.contains("Error: quntity must be at least 1"));
    }

    @Test
    void nonIntegerQuantityIsRejected() {
        String out = session(String.join("\n", "A", "IL", "1", "1", "Book", "two", "7") + "\n");
        assertTrue(out.contains("Error: quantity must be a valid integer"));
    }

    @Test
    void smallestAcceptablePurchaseIsOneDollar() {
        assertThrows(IllegalStateException.class, () -> pricing.validatePurchaseAmount(0.99));
        assertDoesNotThrow(() -> pricing.validatePurchaseAmount(1.00));
    }

    @Test
    void largestAcceptablePurchaseIsThe99kCap() {
        assertDoesNotThrow(() -> pricing.validatePurchaseAmount(99999.99));
        assertThrows(IllegalStateException.class, () -> pricing.validatePurchaseAmount(100000.00));
    }

    @Test
    void taxStatesAllChargeSixPercent() {
        assertEquals(0.06, pricing.getTaxRate("IL"));
        assertEquals(0.06, pricing.getTaxRate("CA"));
        assertEquals(0.06, pricing.getTaxRate("NY"));
    }

    @Test
    void everyOtherStateIsTaxFree() {
        assertEquals(0.0, pricing.getTaxRate("TX"));
    }

    @Test
    void standardShippingIsTenAndFreeOverFifty() {
        assertEquals(10.00, pricing.calculateShipping(ShippingOption.STANDARD, 50.00));
        assertEquals(0.00, pricing.calculateShipping(ShippingOption.STANDARD, 50.01));
    }

    @Test
    void nextDayShippingIsAlwaysTwentyFive() {
        assertEquals(25.00, pricing.calculateShipping(ShippingOption.NEXT_DAY, 1.00));
        assertEquals(25.00, pricing.calculateShipping(ShippingOption.NEXT_DAY, 9999.00));
    }
}
