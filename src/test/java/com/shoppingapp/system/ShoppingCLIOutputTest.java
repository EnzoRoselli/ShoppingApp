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

class ShoppingCLIOutputTest {

    private String run(String script) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(captured, true, StandardCharsets.UTF_8);
        new ShoppingCLI(scanner, out, new ItemCatalog(), new PricingService()).run();
        return captured.toString(StandardCharsets.UTF_8).replace(System.lineSeparator(), "\n");
    }

    @Test
    void promptsGreetingAndMenuArePrintedExactly() {
        String out = run("Enzo\nIL\n1\n7\n");

        assertTrue(out.contains("Enter your name: "));
        assertTrue(out.contains("Shipping option:\n1 - Standard\n2 - Next Day\n"));
        assertTrue(out.contains("choice: \nHello Enzo - Shipping: STANDARD, State: IL (6% tax)\n"));
        assertTrue(out.contains("(6% tax)\n\nWhat would you like to do?\n"));
        assertTrue(out.contains("What would you like to do?\n1 - Add item to cart\n2 - View cart\n3 - Get current total\n4 - Edit item quantity\n5 - Remove item\n6 - Checkout\n7 - Quit\n"));
        assertTrue(out.endsWith("Goodbye Enzo\n\n"));
    }

    @Test
    void addItemPrintsTheWholeCatalog() {
        String out = run("Enzo\nWA\n1\n1\nBook\n2\n7\n");
        assertTrue(out.contains("Available items:\n Book - $12.00\n Laptop - $800.00\n Shirt - $25.50\n Headphones - $49.99\n Phone - $599.99\n Charger - $19.99\n"));
    }

    @Test
    void viewOnEmptyCartPrintsTheMessage() {
        assertTrue(run("Enzo\nWA\n1\n2\n7\n").contains("cart is empty"));
    }

    @Test
    void totalOnEmptyCartPrintsTheMessage() {
        assertTrue(run("Enzo\nWA\n1\n3\n7\n").contains("cart is empty"));
    }

    @Test
    void editOnEmptyCartPrintsTheMessage() {
        assertTrue(run("Enzo\nWA\n1\n4\n7\n").contains("cart is empty"));
    }

    @Test
    void removeOnEmptyCartPrintsTheMessage() {
        assertTrue(run("Enzo\nWA\n1\n5\n7\n").contains("cart is empty"));
    }

    @Test
    void getTotalPrintsTheFullSummaryBlock() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n3\n7\n");
        assertTrue(out.contains("Order summary:\nSubtotal: $24.00\nTax: $1.44\nShipping: $10.00\nTotal: $35.44\n"));
    }

    @Test
    void checkoutPrintsSummaryBlankLineThenCompleted() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n6\n7\n");
        assertTrue(out.contains("Order summary:\nSubtotal: $24.00\nTax: $1.44\nShipping: $10.00\nTotal: $35.44\n\nTransaction completed.\n"));
    }

    @Test
    void editShowsTheCartBeforeAsking() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n4\nBook\n5\n7\n");
        assertTrue(out.contains("Cart:\n Book x2 = $24.00\n"));
    }

    @Test
    void removeShowsTheCartBeforeAsking() {
        String out = run("Enzo\nIL\n1\n1\nBook\n2\n5\nBook\n7\n");
        assertTrue(out.contains("Cart:\n Book x2 = $24.00\n"));
    }
}
