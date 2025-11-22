package io.gith.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.gith.Main;
import io.gith.entity.entity.Player;
import io.gith.entity.inventory.Inventory;
import io.gith.entity.inventory.Slot;
import lombok.Getter;

import java.util.ArrayList;

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


    /** ============================================================
     * INVENTORY BACKGROUND
     * ============================================================ */
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

    /** ============================================================
     * NORMAL INVENTORY SLOT
     * ============================================================ */
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

    /** ============================================================
     * ARMOR SLOTS
     * ============================================================ */
    protected void renderArmorSlots() {
        SpriteBatch batch = Main.getInstance().getBatch();
        ArrayList<Slot> armorSlots = inventoryUI.getInventory().getArmorSlots();

        int armorCount = armorSlots.size();
        float slotSize = BASE_SLOTSIZE * scale.getValue();
        float padding = BASE_PADDING * scale.getValue();

        int invCols = inventoryUI.getInventory().getSlotsWidth();
        int invRows = inventoryUI.getInventory().getSlotsHeight();

        float invWidth = invCols * slotSize + (invCols - 1) * padding;
        float invHeight = invRows * slotSize + (invRows - 1) * padding;

        float invStartX = (VIRTUAL_GUI_WIDTH - invWidth) / 2f;
        float invStartY = (VIRTUAL_GUI_HEIGHT - invHeight) / 2f;

        // Armor total height
        float totalArmorHeight = armorCount * slotSize + (armorCount - 1) * padding;

        // Armor background dimensions
        float armorBgWidth = slotSize + padding * 2f;
        float armorBgHeight = totalArmorHeight + padding * 2f;

        // Armor background position
        float armorBgX = invStartX + invWidth + padding * 2f;
        float armorBgY = invStartY + (invHeight - armorBgHeight) / 2f;

        float sx = Main.getInstance().getGuiCamera().viewportWidth / VIRTUAL_GUI_WIDTH;
        float sy = Main.getInstance().getGuiCamera().viewportHeight / VIRTUAL_GUI_HEIGHT;

        // render background for armor slots
        TextureRegion bg = Main.getInstance().getAssetsController().getGUIRegion("background");
        batch.draw(bg,
            armorBgX * sx,
            armorBgY * sy,
            armorBgWidth * sx,
            armorBgHeight * sy
        );

        // draw armor slots
        for (int i = 0; i < armorCount; i++) {
            Slot slot = armorSlots.get(i);

            float armorX = armorBgX + padding;
            float armorY = armorBgY + padding + (armorCount - 1 - i) * (slotSize + padding);

            batch.draw(
                Main.getInstance().getAssetsController().getGUIRegion("slot"),
                armorX * sx,
                armorY * sy,
                slotSize * sx,
                slotSize * sy
            );
        }
    }


    /** ============================================================
     * TOP BUTTON BAR
     * ============================================================ */
    protected void renderButtons() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion button = Main.getInstance().getAssetsController().getGUIRegion("button");
        BitmapFont font = Main.getInstance().getAssetsController().getFont();

        float slotSize = BASE_SLOTSIZE * GUI.getScale().getValue();
        float padding = BASE_PADDING * GUI.getScale().getValue();

        // button dimensions
        float btnWidth = slotSize * 3f;
        float btnHeight = slotSize * (2f / 3f);

        int count = 3;
        float btnSpacing = padding * 4f;

        float totalWidth = count * btnWidth + (count - 1) * btnSpacing;

        int rows = inventoryUI.getInventory().getSlotsHeight();
        float invHeight = rows * slotSize + (rows - 1) * padding;
        float startY = (VIRTUAL_GUI_HEIGHT + invHeight) / 2f + padding * 1f;
        float startX = (VIRTUAL_GUI_WIDTH - totalWidth) / 2f;

        float guiScaleX = Main.getInstance().getGuiCamera().viewportWidth / VIRTUAL_GUI_WIDTH;
        float guiScaleY = Main.getInstance().getGuiCamera().viewportHeight / VIRTUAL_GUI_HEIGHT;

        String[] labels = {"Inventory", "Stats", "Crafting"};

        for (int i = 0; i < count; i++) {
            float x = (startX + i * (btnWidth + btnSpacing)) * guiScaleX;
            float y = startY * guiScaleY;
            float w = btnWidth * guiScaleX;
            float h = btnHeight * guiScaleY;

            // draw button
            batch.draw(button, x, y, w, h);

            // draw text centered
            String label = labels[i];
            font.setColor(new Color(235/255f, 223/255f, 223/255f, 1f));

            // reset font scale to 1 before layout
            font.getData().setScale(1f);
            GlyphLayout layout = new GlyphLayout(font, label);

            float textScaleX = (w * 0.9f) / layout.width;
            float textScaleY = (h * 0.6f) / layout.height;
            float finalTextScale = Math.min(textScaleX, textScaleY);

            font.getData().setScale(finalTextScale);

            layout.setText(font, label);

            float textX = x + w / 2f - layout.width / 2f;
            float textY = y + h / 2.6f + layout.height / 2f;

            font.draw(batch, layout, textX, textY);
        }
    }

}
