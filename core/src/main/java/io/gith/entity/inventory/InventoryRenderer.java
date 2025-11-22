package io.gith.entity.inventory;

import io.gith.Renderable;

public class InventoryRenderer implements Renderable
{
    private Inventory inventory;

    public InventoryRenderer(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void renderTexture() {

    }

    @Override
    public void renderShape() {

    }
}
