package com.shoppingapp.system;

import com.shoppingapp.cli.ShoppingCLI;
import com.shoppingapp.service.ItemCatalog;
import com.shoppingapp.service.PricingService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCLISystemTest {

    private String run(String script) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(captured, true, StandardCharsets.UTF_8);
        new ShoppingCLI(scanner, out, new ItemCatalog(), new PricingService()).run();
        return captured.toString(StandardCharsets.UTF_8);
    }

    @Test
    void fullHappyPathAddViewTotalCheckout() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n2\n3\n6\n7\n");

        assertTrue(out.contains("Hello Enzo - Shipping: STANDARD, State: IL (6% tax)"));
        assertTrue(out.contains("added! cart has 2 item(s)"));
        assertTrue(out.contains("Cart:"));
        assertTrue(out.contains("Book x2 = $24.00"));
        assertTrue(out.contains("Transaction completed."));
        assertTrue(out.contains("Goodbye Enzo"));
    }

    @Test
    void nextDayShippingAndNoTaxStateShowInGreeting() {
        String out = run("Sam\nWA\n2\n7\n");
        assertTrue(out.contains("Hello Sam - Shipping: NEXT_DAY, State: WA (0% tax)"));
    }

    @Test
    void invalidMenuOptionIsReported() {
        String out = run("Enzo\nIL\n1\n9\n7\n");
        assertTrue(out.contains("invalid option, pick between 1-7"));
    }

    @Test
    void addingQuantityBelowOneErrors() {
        String out = run("Enzo\nIL\n1\n1\nBook\n0\n7\n");
        assertTrue(out.contains("Error: quntity must be at least 1"));
    }

    @Test
    void addingNonIntegerQuantityErrors() {
        String out = run("Enzo\nIL\n1\n1\nBook\nabc\n7\n");
        assertTrue(out.contains("Error: quantity must be a valid integer"));
    }

    @Test
    void addingUnknownItemErrors() {
        String out = run("Enzo\nIL\n1\n1\nSpaceship\n1\n7\n");
        assertTrue(out.contains("not found in catalog"));
    }

    @Test
    void addingSameItemTwiceStacksTheCount() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n1\nBook\n3\n7\n");
        assertTrue(out.contains("added! cart has 5 item(s)"));
    }

    @Test
    void viewTotalEditRemoveCheckoutOnEmptyCartAllComplain() {
        String out = run("Enzo\nIL\n1\n2\n3\n4\n5\n6\n7\n");
        assertTrue(out.contains("cart is empty"));
        assertTrue(out.contains("cart is empty, nothing to checkout"));
    }

    @Test
    void editQuantityUpdatesItem() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n4\nBook\n5\n3\n7\n");
        assertTrue(out.contains("quantity updated"));
        assertTrue(out.contains("Subtotal: $60.00"));
    }

    @Test
    void editQuantityWithBadNumberErrors() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n4\nBook\nxx\n7\n");
        assertTrue(out.contains("Error: quantity must be a valid integer"));
    }

    @Test
    void editQuantityBelowOneErrors() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n4\nBook\n0\n7\n");
        assertTrue(out.contains("Error: quantity must be at least 1"));
    }

    @Test
    void editMissingItemErrors() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n4\nLaptop\n3\n7\n");
        assertTrue(out.contains("not found in cart"));
    }

    @Test
    void removeItemWorks() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n5\nBook\n7\n");
        assertTrue(out.contains("removed"));
    }

    @Test
    void removeMissingItemErrors() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n5\nLaptop\n7\n");
        assertTrue(out.contains("not found in cart"));
    }

    @Test
    void checkoutOverMaximumIsBlocked() {
        String out = run("Enzo\nIL\n1\n1\nLaptop\n200\n6\n7\n");
        assertTrue(out.contains("cant exceed"));
        assertFalse(out.contains("Transaction completed."));
    }
}
