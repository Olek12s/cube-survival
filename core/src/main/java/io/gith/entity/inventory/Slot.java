package io.gith.entity.inventory;

import item.Item;
import lombok.Getter;

@Getter
public class Slot
{
    private final Inventory inventory;
    private final int index;
    private boolean occupied;
    private Item item;

    public Slot(Inventory inventory, int index) {
        this.inventory = inventory;
        this.index = index;
    }
}
