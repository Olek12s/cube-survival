package io.gith.tile;

import io.gith.Main;
import io.gith.utils.Pair;

import java.util.HashMap;
import java.util.Map;


public class Autotiler
{
    private static final Map<Byte, Pair<Integer, Integer>> maskToVariant = new HashMap<>();
    private static final int ATLAS_WIDTH = 7;

    static {
        maskToVariant.put((byte) 0b1110_0011, new Pair<>(0, 0));
        maskToVariant.put((byte)0b1000_0011, new Pair<>(1, 0));
        maskToVariant.put((byte)0b1000_1111, new Pair<>(2, 0));
        maskToVariant.put((byte)0b1110_1111, new Pair<>(3, 0));
        maskToVariant.put((byte)0b0000_1000, new Pair<>(4, 0));
        maskToVariant.put((byte)0b0010_0000, new Pair<>(5, 0));
        maskToVariant.put((byte)0b1010_0000, new Pair<>(6, 0));

        maskToVariant.put((byte)0b1110_0000, new Pair<>(0, 1));
        maskToVariant.put((byte)0b1111_1111, new Pair<>(1, 1));
        maskToVariant.put((byte)0b0000_1110, new Pair<>(2, 1));
        maskToVariant.put((byte)0b1110_1110, new Pair<>(3, 1));
        maskToVariant.put((byte)0b0000_0010, new Pair<>(4, 1));
        maskToVariant.put((byte)0b1000_0000, new Pair<>(5, 1));
        maskToVariant.put((byte)0b1000_0010, new Pair<>(6, 1));

        maskToVariant.put((byte)0b1111_1000, new Pair<>(0, 2));
        maskToVariant.put((byte)0b0011_1000, new Pair<>(1, 2));
        maskToVariant.put((byte)0b0011_1110, new Pair<>(2, 2));
        maskToVariant.put((byte)0b1111_1110, new Pair<>(3, 2));
        maskToVariant.put((byte)0b1000_1010, new Pair<>(4, 2));
        maskToVariant.put((byte)0b1010_1000, new Pair<>(5, 2));
        maskToVariant.put((byte)0b0000_1010, new Pair<>(6, 2));

        maskToVariant.put((byte)0b1111_1011, new Pair<>(0, 3));
        maskToVariant.put((byte)0b0100_0100, new Pair<>(1, 3));
        maskToVariant.put((byte)0b0100_0000, new Pair<>(2, 3));
        maskToVariant.put((byte)0b0000_0000, new Pair<>(3, 3));
        maskToVariant.put((byte)0b0010_1010, new Pair<>(4, 3));
        maskToVariant.put((byte)0b1010_0010, new Pair<>(5, 3));
        maskToVariant.put((byte)0b0010_1000, new Pair<>(6, 3));

        maskToVariant.put((byte)0b1110_1000, new Pair<>(0, 4));
        maskToVariant.put((byte)0b0010_1110, new Pair<>(1, 4));
        maskToVariant.put((byte)0b1000_1011, new Pair<>(2, 4));
        maskToVariant.put((byte)0b1010_0011, new Pair<>(3, 4));
        maskToVariant.put((byte)0b1110_1011, new Pair<>(4, 4));
        maskToVariant.put((byte)0b1010_1011, new Pair<>(5, 4));
        maskToVariant.put((byte)0b1010_1111, new Pair<>(6, 4));

        maskToVariant.put((byte)0b0111_0010, new Pair<>(0, 5));
        maskToVariant.put((byte)0b1000_1110, new Pair<>(1, 5));
        maskToVariant.put((byte)0b0011_1010, new Pair<>(2, 5));
        maskToVariant.put((byte)0b1011_1000, new Pair<>(3, 5));
        maskToVariant.put((byte)0b1110_1010, new Pair<>(4, 5));
        maskToVariant.put((byte)0b1010_1010, new Pair<>(5, 5));
        maskToVariant.put((byte)0b1010_1110, new Pair<>(6, 5));

        //maskToVariant.put(0b1111_1111, new Pair<>(0, 6));   // null
        //maskToVariant.put(0b1111_1111, new Pair<>(1, 6));   // null
        maskToVariant.put((byte)0b0010_0010, new Pair<>(2, 6));
        maskToVariant.put((byte)0b1000_1000, new Pair<>(3, 6));
        maskToVariant.put((byte)0b1111_1010, new Pair<>(4, 6));
        maskToVariant.put((byte)0b1011_1010, new Pair<>(5, 6));
        maskToVariant.put((byte)0b1011_1110, new Pair<>(6, 6));
    }


    public static int getTextureVariantFromMask(byte mask) {
        if (mask < 0) System.out.println("NEgATIVE");
        Pair<Integer, Integer> variant = maskToVariant.get(mask);
;
        if (variant == null) {
            return 0;
        }
        return variant.first() + variant.second() * ATLAS_WIDTH;
    }


    /**
     *  bitmask goes clockwise from North. LSB - North MSB - NorthWest
     *
     *  N   - 0. bit (LSB)
     *  NE  - 1. bit
     *  E   - 2. bit
     *  SE  - 3. bit
     *  S   - 4. bit
     *  SW  - 5. bit
     *  W   - 6. bit
     *  NW  - 7. bit (MSB)
     *
     * @param tile   target tile
     */
    public static void assignBitmask(Tile tile) {
        Tile[] neighboringTiles = Main.getInstance().getTileMap().getTileNeighbors(tile);

        byte bitmask = 0b0000_0000;
        for (int i = 0; i < neighboringTiles.length; i++) {
            if (neighboringTiles[i] == null || neighboringTiles[i].getId() == tile.getId()) {
                continue;
            }
            else    // filling bit in bitmask
            {
                bitmask = (byte) (bitmask | (1 << i));
            }
        }

        String bin = String.format("%8s", Integer.toBinaryString(bitmask & 0xFF)).replace(' ', '0');
        System.out.println("Tile " + tile.getIndexPosition() + " -> bitmask: " + bin);

        System.out.println(">>>>>tile: " + tile.getIndexPosition());
        for (Tile n : neighboringTiles) {
            if (n == null) System.out.println("null");
            else System.out.println(": " + n.getIndexPosition());
        }

        tile.setBitmask(bitmask);
    }
}
