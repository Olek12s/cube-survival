package io.gith.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.gith.Main;
import io.gith.entity.inventory.Slot;

public class SlotUI
{
    public static void renderSlot(Slot slot) {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion region = Main.getInstance().getAssetsController().getGUIRegion("slot");

        int index = slot.getIndex();
        int cols = slot.getInventory().getSlotsWidth();
        int rows = slot.getInventory().getSlotsHeight();

        int slotSize = 32;
        int padding = 4;

        int row = index / cols;
        int col = index % cols;

        float centerX = Main.getInstance().getGuiCamera().viewportWidth / 2f;
        float centerY = Main.getInstance().getGuiCamera().viewportHeight / 2f;

        float totalWidth = cols * (slotSize + padding);
        float totalHeight = rows * (slotSize + padding);

        float startX = centerX - totalWidth / 2f;
        float startY = centerY - totalHeight / 2f;

        float x = startX + col * (slotSize + padding);
        float y = startY + (rows - 1 - row) * (slotSize + padding);

        batch.draw(region, x, y, slotSize, slotSize);
    }
}
