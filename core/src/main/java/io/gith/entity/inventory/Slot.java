package io.gith.entity.inventory;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.gith.Main;
import item.Item;

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


    protected void render() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion region = Main.getInstance().getAssetsController().getGUIRegion("slot");

        int slotSize = 32;
        int padding = 4;
        int cols = inventory.getSlotsWidth();
        int rows = inventory.getSlotsHeight();
        int row = index / cols;
        int col = index % cols;
        int totalWidth = cols * (slotSize + padding);
        int totalHeight = rows * (slotSize + padding);
        float centerX = Main.getInstance().getGuiCamera().viewportWidth / 2f;
        float centerY = Main.getInstance().getGuiCamera().viewportHeight / 2f;
        float startX = centerX - totalWidth / 2f;
        float startY = centerY - totalHeight / 2f;
        float x = startX + col * (slotSize + padding);
        float y = startY + (rows - 1 - row) * (slotSize + padding);

        batch.draw(region, x, y, slotSize, slotSize);


        // batch.draw(frame, entity.getWorldPosition().x, entity.getWorldPosition().y);
    }
}
