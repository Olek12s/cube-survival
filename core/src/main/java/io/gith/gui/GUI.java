package io.gith.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.gith.Main;
import io.gith.entity.entity.Player;
import io.gith.entity.inventory.Inventory;
import io.gith.entity.inventory.Slot;
import lombok.Getter;

@Getter
public class GUI
{
    private static Scale scale;
    private static InventoryUI inventoryUI;

    private static final float VIRTUAL_GUI_WIDTH = 1920f;
    private static final float VIRTUAL_GUI_HEIGHT = 1080f;
    private static final int BASE_SLOTSIZE = 32;
    private static final int BASE_PADDING = 4;

    public static Scale getScale() {return scale;}
    public static void setScale(Scale newScale) {scale = newScale;}


    public GUI(Inventory inventory) {
        scale = Scale.SMALL;
        inventoryUI = new InventoryUI(inventory);
    }

    public InventoryUI getInventoryUI()
    {
        return inventoryUI;
    }

    protected void renderInventoryBackground() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion background = Main.getInstance().getAssetsController().getGUIRegion("background");

        int cols = inventoryUI.getInventory().getSlotsWidth();
        int rows = inventoryUI.getInventory().getSlotsHeight();

        float slotSize = BASE_SLOTSIZE * GUI.getScale().getValue();
        float padding = BASE_PADDING * GUI.getScale().getValue();


        float totalWidth = cols * slotSize + (cols - 1) * padding;
        float totalHeight = rows * slotSize + (rows - 1) * padding;

        float startX = (VIRTUAL_GUI_WIDTH - totalWidth) / 2f;
        float startY = (VIRTUAL_GUI_HEIGHT - totalHeight) / 2f;


        float scaleX = Main.getInstance().getGuiCamera().viewportWidth / VIRTUAL_GUI_WIDTH;
        float scaleY = Main.getInstance().getGuiCamera().viewportHeight / VIRTUAL_GUI_HEIGHT;

        batch.draw(background, startX * scaleX - padding/2f * scaleX, startY * scaleY - padding/2f * scaleY,
            totalWidth * scaleX + padding * scaleX, totalHeight * scaleY + padding * scaleY);
    }

    protected static void renderSlot(Slot slot) {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion region = Main.getInstance().getAssetsController().getGUIRegion("slot");

        int index = slot.getIndex();
        int cols = slot.getInventory().getSlotsWidth();
        int rows = slot.getInventory().getSlotsHeight();

        float slotSize = BASE_SLOTSIZE * GUI.getScale().getValue();
        float padding = BASE_PADDING * GUI.getScale().getValue();

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

    protected void renderButtons() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion button = Main.getInstance().getAssetsController().getGUIRegion("button");

        float slotSize = BASE_SLOTSIZE * GUI.getScale().getValue();
        float padding = BASE_PADDING * GUI.getScale().getValue();

        // button dimensions
        float btnWidth = slotSize * 3f;
        float btnHeight = slotSize * (2f/3f);

        // buttons count
        int count = 3;


        float btnSpacing = padding * 4f;


        float totalWidth = count * btnWidth + (count - 1) * btnSpacing;


        int cols = inventoryUI.getInventory().getSlotsWidth();
        int rows = inventoryUI.getInventory().getSlotsHeight();

        float invHeight = rows * slotSize + (rows - 1) * padding;
        float startY = (VIRTUAL_GUI_HEIGHT + invHeight) / 2f + padding * 4f;


        float startX = (VIRTUAL_GUI_WIDTH - totalWidth) / 2f;


        float scaleX = Main.getInstance().getGuiCamera().viewportWidth / VIRTUAL_GUI_WIDTH;
        float scaleY = Main.getInstance().getGuiCamera().viewportHeight / VIRTUAL_GUI_HEIGHT;

        String[] labels = {"Inventory", "Stats", "Crafting"};

        for (int i = 0; i < count; i++) {
            float x = (startX + i * (btnWidth + btnSpacing)) * scaleX;
            float y = startY * scaleY;

            float w = btnWidth * scaleX;
            float h = btnHeight * scaleY;

            batch.draw(button, x, y, w, h);

            // TODO: draw text
        }
    }

}
