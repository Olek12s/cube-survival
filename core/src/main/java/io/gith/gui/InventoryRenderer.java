package io.gith.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.gith.Main;
import io.gith.Order;
import io.gith.Renderable;
import io.gith.RenderingOrder;
import io.gith.entity.entity.Player;
import io.gith.entity.inventory.Inventory;
import io.gith.entity.inventory.Slot;

import java.util.ArrayList;

@RenderingOrder(Order.GUI)
public class InventoryRenderer extends GUI implements Renderable
{
    private Inventory inventory;

    public InventoryRenderer(Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        Main.getInstance().getRenderablesGUI().add(this);
    }

    @Override
    public void renderTexture() {
        Player player = (Player) inventory.getEntity();
        if (!player.getGui().getInventoryUI().isOpen()) return;

        renderInventoryBackground();
        renderButtons();
        renderArmorSlots();
        for (Slot slot : inventory.getSlots()) {
            renderSlot(slot);
        }
    }





    @Override
    public void renderShape() {

    }


}
