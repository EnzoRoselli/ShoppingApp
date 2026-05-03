package com.shoppingapp.service;

import com.shoppingapp.model.ShippingOption;

public class PricingService {

    public double getTaxRate(String state) {
        String upper = state.toUpperCase();
        if (upper.equals("IL") || upper.equals("CA") || upper.equals("NY")) {
            return 0.06;
        }
        return 0.0;
    }

    public double calculateShipping(ShippingOption option, double rawTotal) {
        if (option == ShippingOption.NEXT_DAY) {
            return 25.00;
        }
        if (rawTotal > 50.00) {
            return 0.00;
        }
        return 10.00;
    }

    public double calculateTotal(double rawTotal, String state, ShippingOption option) {
        double tax = rawTotal * getTaxRate(state);
        double shipping = calculateShipping(option, rawTotal);
        return rawTotal + tax + shipping;
    }

    public void validatePurchaseAmount(double total) {
        if (total < 1.00) {
            throw new IllegalStateException("purchase amount must be at least $1.00");
        }
        if (total > 99999.99) {
            throw new IllegalStateException("purchase amount cant exceed $99,999.99");
        }
    }
}
