package io.gith.tile;

import com.badlogic.gdx.math.Vector2;
import io.gith.Main;

/**
 * N  - 0
 * NE - 1
 * E  - 2
 * SE - 3
 * S  - 4
 * SW - 5
 * W  - 6
 * NW - 7
 */



public class Autotiler
{
    /**
     *
     *  Calculates bitmask based on the tiles's 8 neighbors from the map. If neighbor
     *  is null, then corresponding tile is seen as present of the same type.
     *
     *  bit 0 - tile of the same type
     *  bit 1 - tile of different type (trimming for source tile shall be involved)
     *
     * @param tile tile object from the world map
     * @return bitmask
     */
    public static byte calcBitmask(Tile tile) {
        TileMapController tileMapController = Main.getInstance().getTileMap();
        Tile[] tiles = tileMapController.getTileNeighbors(tile);


        byte bitmask = 0b0000_0000;
        for (int i = 0; i < 8; i++) {
            if (tiles[i] == null || tiles[i].getId() == tile.getId()) { // same or not loaded
                continue;
            }
            else {
                bitmask |= (byte) (1 << i);
            }
        }
        return bitmask;
    }
}
