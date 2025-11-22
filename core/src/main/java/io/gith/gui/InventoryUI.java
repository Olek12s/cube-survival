package io.gith.gui;

import io.gith.entity.inventory.Inventory;

public class InventoryUI
{
    private final Inventory inventory;
    private boolean open = false;

    public InventoryUI(Inventory inventory) {
        this.inventory = inventory;

    }

    public void toggle()
    {
        open = !open;

        String stateColor = open ? "\u001B[32m" : "\u001B[33m"; // green - true // orange - false
        System.out.println(
            "\u001B[33mGUI:\u001B[0m " + // yellow
                "Toggled Inventory: " +
                stateColor + open + "\u001B[0m"
        );
    }

    public boolean isOpen() { return open; }
}
