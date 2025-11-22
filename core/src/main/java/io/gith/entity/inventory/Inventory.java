package io.gith.entity.inventory;

import io.gith.Main;
import io.gith.entity.entity.Entity;
import io.gith.entity.entity.EntityID;
import item.Item;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Inventory
{
    private Entity entity;
    private final int slotsWidth = 10;
    private final int slotsHeight = 5;
    private final ArrayList<Slot> slots;
    private InventoryRenderer inventoryRenderer;
    private InventoryUpdater inventoryUpdater;

    public int getSize() {return slotsWidth * slotsHeight;}
    public Slot getSlotAt(int row, int col) {
        if (row < 0 || row >= slotsHeight) return null;
        if (col < 0 || col >= slotsWidth) return null;

        int index = row * slotsWidth + col;
        return slots.get(index);
    }

    public Inventory(Entity entity) {
        int size = slotsWidth * slotsHeight;
        slots = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            slots.add(new Slot(this, i));
        }
        this.inventoryRenderer = new InventoryRenderer(this);
        this.inventoryUpdater = new InventoryUpdater(this);
        if (entity.getId() == EntityID.PLAYER) {
            Main.getInstance().getRenderables().add(inventoryRenderer);
        }
    }
}
