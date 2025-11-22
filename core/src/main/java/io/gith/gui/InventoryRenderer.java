package io.gith.gui;

import io.gith.Main;
import io.gith.Order;
import io.gith.Renderable;
import io.gith.RenderingOrder;
import io.gith.entity.entity.Player;
import io.gith.entity.inventory.Inventory;
import io.gith.entity.inventory.Slot;

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
        Player player = (Player) inventory.getEntity();
        if (!player.getGui().getInventoryUI().isOpen()) return;

        inventory.getSlots().forEach(SlotUI::renderSlot);
    }

    @Override
    public void renderShape() {

    }
}
