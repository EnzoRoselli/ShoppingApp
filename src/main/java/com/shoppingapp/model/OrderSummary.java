package com.shoppingapp.model;

public class OrderSummary {
    private final double subtotal;
    private final double tax;
    private final double shipping;
    private final double total;

    public OrderSummary(double subtotal, double tax, double shipping, double total) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.shipping = shipping;
        this.total = total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getShipping() {
        return shipping;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return String.format("Subtotal: $%.2f | Tax: $%.2f | Shipping: $%.2f | Total: $%.2f",
                subtotal, tax, shipping, total);
    }
}
