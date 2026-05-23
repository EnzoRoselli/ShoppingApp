package com.shoppingapp.cli;

import com.shoppingapp.model.CartItem;
import com.shoppingapp.model.Item;
import com.shoppingapp.model.OrderSummary;
import com.shoppingapp.model.ShippingOption;
import com.shoppingapp.model.ShoppingCart;
import com.shoppingapp.service.ItemCatalog;
import com.shoppingapp.service.PricingService;

import java.io.PrintStream;
import java.util.Scanner;

public class ShoppingCLI {
    private final PrintStream printStreamOut;
    private final ItemCatalog itemCatalog;
    private final PricingService pricingService;
    private final ShoppingCart shoppingCart;
    private final ConsoleInput input;

    private String customerName;
    private String state;
    private ShippingOption shippingOption;

    public ShoppingCLI() {
        this(new Scanner(System.in), System.out, new ItemCatalog(), new PricingService());
    }

    public ShoppingCLI(Scanner scanner, PrintStream printStreamOut, ItemCatalog itemCatalog, PricingService pricingService) {
        this.printStreamOut = printStreamOut;
        this.itemCatalog = itemCatalog;
        this.pricingService = pricingService;
        this.shoppingCart = new ShoppingCart();
        this.input = new ConsoleInput(scanner, printStreamOut);
    }

    public void run() {
        //main loop. first get customer info, then show the menu
        setupCustomer();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = input.prompt("choice: ");

            switch (choice) {
                case "1" -> handleAddItem();
                case "2" -> handleViewCart();
                case "3" -> handleGetTotal();
                case "4" -> handleEditQuantity();
                case "5" -> handleRemoveItem();
                case "6" -> handleCheckout();
                case "7" -> {
                    printStreamOut.println("Goodbye " + customerName);
                    running = false;
                }
                default -> printStreamOut.println("invalid option, pick between 1-7");
            }
            printStreamOut.println();
        }
    }

    private void setupCustomer() {
        customerName = input.prompt("Enter your name: ");
        state = input.prompt("Enter state (e.g. IL, CA, NY): ").toUpperCase();

        printStreamOut.println("Shipping option:");
        printStreamOut.println("1 - Standard");
        printStreamOut.println("2 - Next Day");
        String shipChoice = input.prompt("choice: ");

        if (shipChoice.equals("2")) {
            shippingOption = ShippingOption.NEXT_DAY;
        } else {
            shippingOption = ShippingOption.STANDARD;
        }

        double taxRate = pricingService.getTaxRate(state) * 100;
        printStreamOut.println();
        printStreamOut.printf("Hello %s - Shipping: %s, State: %s (%.0f%% tax)%n",
                customerName, shippingOption, state, taxRate);
        printStreamOut.println();
    }

    private void printMenu() {
        printStreamOut.println("What would you like to do?");
        printStreamOut.println("1 - Add item to cart");
        printStreamOut.println("2 - View cart");
        printStreamOut.println("3 - Get current total");
        printStreamOut.println("4 - Edit item quantity");
        printStreamOut.println("5 - Remove item");
        printStreamOut.println("6 - Checkout");
        printStreamOut.println("7 - Quit");
    }

    private void handleAddItem() {
        printStreamOut.println("Available items:");
        for (Item item : itemCatalog.getAllItems()) {
            printStreamOut.println(" " + item);
        }

        String itemName = input.prompt("item name: ");
        String qtyInput = input.prompt("quntity: ");

        // using parseInt instead of nextInt to avoid Scanner issues with leftover newlines
        try {
            int quantity = Integer.parseInt(qtyInput);
            if (quantity < 1) {
                printStreamOut.println("Error: quntity must be at least 1");
                return;
            }
            Item item = itemCatalog.findItem(itemName);
            int totalCount = shoppingCart.addItem(item, quantity);
            printStreamOut.println("added! cart has " + totalCount + " item(s)");
        } catch (NumberFormatException e) {
            printStreamOut.println("Error: quantity must be a valid integer");
        } catch (IllegalArgumentException e) {
            printStreamOut.println("Error: " + e.getMessage());
        }
    }

    private void handleViewCart() {
        if (shoppingCart.isEmpty()) {
            printStreamOut.println("cart is empty");
            return;
        }
        printStreamOut.println("Cart:");
        for (CartItem cartItem : shoppingCart.getItems()) {
            printStreamOut.println(" " + cartItem);
        }
    }

    private void handleGetTotal() {
        if (shoppingCart.isEmpty()) {
            printStreamOut.println("cart is empty");
            return;
        }

        OrderSummary summary = pricingService.summarize(shoppingCart.getRawTotal(), state, shippingOption);
        printSummary(summary);
    }

    private void handleEditQuantity() {
        if (shoppingCart.isEmpty()) {
            printStreamOut.println("cart is empty");
            return;
        }
        handleViewCart();

        String itemName = input.prompt("item name to edit: ");
        String qtyInput = input.prompt("new quantity: ");

        try {
            int newQuantity = Integer.parseInt(qtyInput);
            shoppingCart.editQuantity(itemName, newQuantity);
            printStreamOut.println("quantity updated");
        } catch (NumberFormatException e) {
            printStreamOut.println("Error: quantity must be a valid integer");
        } catch (IllegalArgumentException e) {
            printStreamOut.println("Error: " + e.getMessage());
        }
    }

    private void handleRemoveItem() {
        if (shoppingCart.isEmpty()) {
            printStreamOut.println("cart is empty");
            return;
        }
        handleViewCart();

        String itemName = input.prompt("item name to remove: ");

        try {
            shoppingCart.removeItem(itemName);
            printStreamOut.println("removed");
        } catch (IllegalArgumentException e) {
            printStreamOut.println("Error: " + e.getMessage());
        }
    }

    private void handleCheckout() {
        if (shoppingCart.isEmpty()) {
            printStreamOut.println("cart is empty, nothing to checkout");
            return;
        }

        OrderSummary summary = pricingService.summarize(shoppingCart.getRawTotal(), state, shippingOption);

        try {
            pricingService.validatePurchaseAmount(summary.getTotal());
        } catch (IllegalStateException e) {
            printStreamOut.println("Error: " + e.getMessage());
            return;
        }

        printSummary(summary);
        printStreamOut.println();
        printStreamOut.println("Transaction completed.");
    }

    private void printSummary(OrderSummary summary) {
        printStreamOut.println("Order summary:");
        printStreamOut.printf("Subtotal: $%.2f%n", summary.getSubtotal());
        printStreamOut.printf("Tax: $%.2f%n", summary.getTax());
        printStreamOut.printf("Shipping: $%.2f%n", summary.getShipping());
        printStreamOut.printf("Total: $%.2f%n", summary.getTotal());
    }
}
