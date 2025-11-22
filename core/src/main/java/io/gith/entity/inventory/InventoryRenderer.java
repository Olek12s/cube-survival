package io.gith.entity.inventory;

import io.gith.Main;
import io.gith.Order;
import io.gith.Renderable;
import io.gith.RenderingOrder;

import java.util.ArrayList;

@RenderingOrder(Order.GUI)
public class InventoryRenderer implements Renderable
{
    private Inventory inventory;

    public InventoryRenderer(Inventory inventory) {
        this.inventory = inventory;
        Main.getInstance().getRenderablesGUI().add(this);
    }

    @Override
    public void renderTexture() {
        ArrayList<Slot> slots = inventory.getSlots();

        for (Slot slot : slots) {
            slot.render();
        }
    }

    @Override
    public void renderShape() {

    }
}
