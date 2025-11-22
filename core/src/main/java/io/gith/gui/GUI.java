package io.gith.gui;

import io.gith.entity.entity.Player;
import lombok.Getter;

@Getter
public class GUI
{
    private static Scale scale;
    private static InventoryUI inventoryUI;

    public static Scale getScale() {return scale;}
    public static void setScale(Scale newScale) {scale = newScale;}


    public GUI(Player player) {
        scale = Scale.SMALL;
        inventoryUI = new InventoryUI(player.getInventory());
    }

    public InventoryUI getInventoryUI()
    {
        return inventoryUI;
    }
}
