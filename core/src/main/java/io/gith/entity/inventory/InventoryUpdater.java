package io.gith.entity.inventory;

import io.gith.Main;
import io.gith.Updatable;

public class InventoryUpdater implements Updatable
{
    private Inventory inventory;

    public InventoryUpdater(Inventory inventory) {
        this.inventory = inventory;
        Main.getInstance().getUpdatables().add(this);
    }

    @Override
    public void update(float dt) {

    }
}
