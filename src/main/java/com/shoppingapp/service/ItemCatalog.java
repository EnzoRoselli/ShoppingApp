package com.shoppingapp.service;

import com.shoppingapp.model.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemCatalog {
    private final Map<String, Item> items = new LinkedHashMap<>();

    // Default catalog with some sample products
    public ItemCatalog() {
        addItem(new Item("Book", 12.00));
        addItem(new Item("Laptop", 800.00));
        addItem(new Item("Shirt", 25.50));
        addItem(new Item("Headphones", 49.99));
        addItem(new Item("Phone", 599.99));
        addItem(new Item("Charger", 19.99));
    }

    public ItemCatalog(List<Item> itemList) {
        for (Item item : itemList) {
            addItem(item);
        }
    }

    private void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item);
    }

    public Item findItem(String name) {
        Item item = items.get(name.toLowerCase());
        if (item == null) {
            throw new IllegalArgumentException("item '" + name + "' not found in catalog");
        }
        return item;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
}
