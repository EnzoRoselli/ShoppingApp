package com.shoppingapp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {
    private final List<CartItem> items = new ArrayList<>();

    // if the item is already in the cart jjust increase its quantity instead of adding a duplicate
    public int addItem(Item item, int quantity) {
        for (CartItem cartItem : items) {
            if (cartItem.getItem().equals(item)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                return getItemCount();
            }
        }
        items.add(new CartItem(item, quantity));
        return getItemCount();
    }

    public void removeItem(String itemName) {
        boolean removed = items.removeIf(ci -> ci.getItem().getName().equalsIgnoreCase(itemName));
        if (!removed) {
            throw new IllegalArgumentException("item '" + itemName + "' not found in cart");
        }
    }

    public void editQuantity(String itemName, int newQuantity) {
        for (CartItem cartItem : items) {
            if (cartItem.getItem().getName().equalsIgnoreCase(itemName)) {
                cartItem.setQuantity(newQuantity);
                return;
            }
        }
        throw new IllegalArgumentException("item '" + itemName + "' not found in cart");
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    //sum of all line totals before tax and shipping
    public double getRawTotal() {
        double total = 0;
        for (CartItem cartItem : items) {
            total += cartItem.getLineTotal();
        }
        return total;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem cartItem : items) {
            count += cartItem.getQuantity();
        }
        return count;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
