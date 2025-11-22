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

        renderInventoryBackground();
        for (Slot slot : inventory.getSlots()) {
            renderSlot(slot);
        }
    }

    private static final float VIRTUAL_GUI_WIDTH = 1920f;
    private static final float VIRTUAL_GUI_HEIGHT = 1080f;

    private void renderInventoryBackground() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion background = Main.getInstance().getAssetsController().getGUIRegion("background");

        int cols = inventory.getSlotsWidth();
        int rows = inventory.getSlotsHeight();

        float slotSize = 32 * GUI.getScale().getValue();
        float padding = 4 * GUI.getScale().getValue();


        float totalWidth = cols * slotSize + (cols - 1) * padding;
        float totalHeight = rows * slotSize + (rows - 1) * padding;

        float startX = (VIRTUAL_GUI_WIDTH - totalWidth) / 2f;
        float startY = (VIRTUAL_GUI_HEIGHT - totalHeight) / 2f;


        float scaleX = Main.getInstance().getGuiCamera().viewportWidth / VIRTUAL_GUI_WIDTH;
        float scaleY = Main.getInstance().getGuiCamera().viewportHeight / VIRTUAL_GUI_HEIGHT;

        batch.draw(background, startX * scaleX - padding/2f * scaleX, startY * scaleY - padding/2f * scaleY,
            totalWidth * scaleX + padding * scaleX, totalHeight * scaleY + padding * scaleY);
    }

    private static void renderSlot(Slot slot) {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion region = Main.getInstance().getAssetsController().getGUIRegion("slot");

        int index = slot.getIndex();
        int cols = slot.getInventory().getSlotsWidth();
        int rows = slot.getInventory().getSlotsHeight();

        float slotSize = 32 * GUI.getScale().getValue();
        float padding = 4 * GUI.getScale().getValue();

        int row = index / cols;
        int col = index % cols;

        float totalWidth = cols * slotSize + (cols - 1) * padding;
        float totalHeight = rows * slotSize + (rows - 1) * padding;

        float startX = (VIRTUAL_GUI_WIDTH - totalWidth) / 2f;
        float startY = (VIRTUAL_GUI_HEIGHT - totalHeight) / 2f;

        float scaleX = Main.getInstance().getGuiCamera().viewportWidth / VIRTUAL_GUI_WIDTH;
        float scaleY = Main.getInstance().getGuiCamera().viewportHeight / VIRTUAL_GUI_HEIGHT;

        float x = (startX + col * (slotSize + padding)) * scaleX;
        float y = (startY + (rows - 1 - row) * (slotSize + padding)) * scaleY;
        float width = slotSize * scaleX;
        float height = slotSize * scaleY;

        batch.draw(region, x, y, width, height);
    }




    @Override
    public void renderShape() {

    }


}
