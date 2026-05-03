package com.shoppingapp.model;

//represents a single line in the shopping cart: wich item and how many of it
public class CartItem {
    private final Item item;
    private int quantity;

    public CartItem(Item item, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1");
        }
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1");
        }
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s x%d = $%.2f", item.getName(), quantity, getLineTotal());
    }
}
