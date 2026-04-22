package com.wmsai.oopj;

/**
 * Demonstrates this() constructor chaining.
 * Covers OOPJ-16 — this keyword for constructor chaining.
 */
public class InventoryItem {
    private int itemId;
    private String name;
    private int quantity;
    private double price;

    /** Default constructor — chains to parameterized. */
    public InventoryItem() {
        this(0, "Unknown", 0, 0.0);
    }

    /** Name-only constructor — chains to full constructor. */
    public InventoryItem(String name) {
        this(0, name, 0, 0.0);
    }

    /** Name + quantity constructor — chains to full constructor. */
    public InventoryItem(String name, int quantity) {
        this(0, name, quantity, 0.0);
    }

    /** Full parameterized constructor. */
    public InventoryItem(int itemId, String name, int quantity, double price) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getItemId() { return itemId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "InventoryItem[id=" + itemId + ", name=" + name +
               ", qty=" + quantity + ", price=" + price + "]";
    }
}
