package io.gith.entity.inventory;

import item.Item;

import java.util.ArrayList;

public class Inventory
{
    private int slotsWidth = 10;
    private int slotsHeight = 5;
    private ArrayList<Slot> slots;
    private InventoryRenderer inventoryRenderer;
    private InventoryUpdater inventoryUpdater;

    public int getSize() {return slotsWidth * slotsHeight;}
    public Slot getSlotAt(int row, int col) {
        if (row < 0 || row >= slotsHeight) return null;
        if (col < 0 || col >= slotsWidth) return null;

        int index = row * slotsWidth + col;
        return slots.get(index);
    }

    public Inventory() {
        int size = slotsWidth * slotsHeight;
        slots = new ArrayList<>(size);
        this.inventoryRenderer = new InventoryRenderer(this);
        this.inventoryUpdater = new InventoryUpdater(this);

    }
}
