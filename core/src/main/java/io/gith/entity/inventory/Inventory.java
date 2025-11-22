package io.gith.entity.inventory;

import io.gith.entity.entity.Entity;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Inventory {
    private final Entity entity;
    private final int slotsWidth = 10;
    private final int slotsHeight = 5;
    private final ArrayList<Slot> slots;
    private final ArrayList<Slot> armorSlots;

    public int getSize() { return slotsWidth * slotsHeight; }

    public Slot getSlotAt(int row, int col) {
        if (row < 0 || row >= slotsHeight) return null;
        if (col < 0 || col >= slotsWidth) return null;
        return slots.get(row * slotsWidth + col);
    }

    public Inventory(Entity entity) {
        this.entity = entity;
        int size = slotsWidth * slotsHeight;

        this.slots = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            slots.add(new Slot(this, i));
        }

        this.armorSlots = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            armorSlots.add(new Slot(this, -1)); // armor slots don't use index
        }
    }
}
