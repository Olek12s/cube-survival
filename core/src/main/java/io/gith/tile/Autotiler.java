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
        maskToVariant.put((byte)0b1110_0011, new Pair<>(0, 0));
        maskToVariant.put((byte)0b1000_0011, new Pair<>(1, 0));
        maskToVariant.put((byte)0b1000_1111, new Pair<>(2, 0));
        maskToVariant.put((byte)0b1110_1111, new Pair<>(3, 0));
        maskToVariant.put((byte)0b0000_1000, new Pair<>(4, 0));
        maskToVariant.put((byte)0b0010_0000, new Pair<>(5, 0));
        maskToVariant.put((byte)0b1010_0000, new Pair<>(6, 0));

        maskToVariant.put((byte)0b1110_0000, new Pair<>(0, 1));
        maskToVariant.put((byte)0b0000_0000, new Pair<>(1, 1));
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
        maskToVariant.put((byte)0b0100_0000, new Pair<>(0, 1));
        maskToVariant.put((byte)0b1111_1111, new Pair<>(3, 3));
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

        maskToVariant.put((byte)0b0000_0001, new Pair<>(1, 0)); // 1    N
        maskToVariant.put((byte)0b0100_0001, new Pair<>(0, 0)); // 65   N W
        maskToVariant.put((byte)0b0000_0101, new Pair<>(2, 0)); // 5    N E
        maskToVariant.put((byte)0b0000_0100, new Pair<>(2, 1)); // 4    E
        maskToVariant.put((byte)0b0001_0000, new Pair<>(1, 2)); // 16   S
        maskToVariant.put((byte)0b0101_0000, new Pair<>(0, 2)); // 80   S W
        maskToVariant.put((byte)0b0001_1100, new Pair<>(2, 2)); // 28   E SE S
        maskToVariant.put((byte)0b0001_0001, new Pair<>(1, 3)); // 17   N S
        maskToVariant.put((byte)0b0000_0111, new Pair<>(2, 0)); // 7    N NE E
        maskToVariant.put((byte)0b0001_0100, new Pair<>(2, 2)); // 20   E S

        maskToVariant.put((byte)0b0111_1111, new Pair<>(4, 3));
        maskToVariant.put((byte)0b1111_0111, new Pair<>(5, 3));
        maskToVariant.put((byte)0b1011_1111, new Pair<>(5, 2));
        maskToVariant.put((byte)0b0101_0001, new Pair<>(0, 3));
        maskToVariant.put((byte)0b0001_0101, new Pair<>(2, 3));
        maskToVariant.put((byte)0b0001_0010, new Pair<>(2, 3));
        maskToVariant.put((byte)0b0000_1001, new Pair<>(2, 4));
        maskToVariant.put((byte)0b0010_0001, new Pair<>(3, 4));
        maskToVariant.put((byte)0b0100_0101, new Pair<>(3, 0));
        maskToVariant.put((byte)0b1000_0100, new Pair<>(1, 5));
        maskToVariant.put((byte)0b0100_1000, new Pair<>(0, 4));
        maskToVariant.put((byte)0b1001_0100, new Pair<>(6, 6));
        maskToVariant.put((byte)0b0100_1001, new Pair<>(4, 4));
        maskToVariant.put((byte)0b0101_0100, new Pair<>(3, 2));
        maskToVariant.put((byte)0b0010_0100, new Pair<>(1, 4));
        maskToVariant.put((byte)0b1001_0000, new Pair<>(3, 5));
        maskToVariant.put((byte)0b0101_0101, new Pair<>(3, 3));
        maskToVariant.put((byte)0b0100_0010, new Pair<>(0, 5));
        maskToVariant.put((byte)0b0010_0101, new Pair<>(6, 4));
        maskToVariant.put((byte)0b0101_0010, new Pair<>(4, 6));
    }


    public static int getTextureVariantFromMask(byte mask) {
        Pair<Integer, Integer> variant = maskToVariant.get(mask);
;
        if (variant == null) {
            String bin = String.format("%8s", Integer.toBinaryString(mask & 0xFF))
                .replace(' ', '0');
            System.out.println("no variant for: " + bin + " (" + mask + ")");
            System.out.println("   bits: N=" + ((mask & 1) != 0) +
                ", NE=" + ((mask & 2) != 0) +
                ", E=" + ((mask & 4) != 0) +
                ", SE=" + ((mask & 8) != 0) +
                ", S=" + ((mask & 16) != 0) +
                ", SW=" + ((mask & 32) != 0) +
                ", W=" + ((mask & 64) != 0) +
                ", NW=" + ((mask & 128) != 0));

            return 0;
        }
        return variant.first() + variant.second() * ATLAS_WIDTH;
    }



    public static void assignBitmask(Tile tile) {
        Tile[] neighboringTiles = Main.getInstance().getTileMap().getTileNeighbors(tile);

        int[] axialIndices = {0, 2, 4, 6}; // N, E, S, W
        byte bitmask = 0b0000_0000;

        for (int idx : axialIndices) {
            Tile neighbor = neighboringTiles[idx];
            if (neighbor != null && neighbor.getId() != tile.getId()) {
                bitmask |= (byte) (1 << idx);   // set bit
            }
        }

        int[][] cornerIndices = {
            {1, 0, 2}, // NE - requires N, E
            {3, 4, 2}, // SE - requires S, E
            {5, 4, 6}, // SW - requires S, W
            {7, 0, 6}  // NW - requires N, W
        };

        for (int[] corner : cornerIndices) {
            int cornerBit = corner[0];
            int axial1 = corner[1];
            int axial2 = corner[2];

            Tile neighbor = neighboringTiles[cornerBit];
            if (neighbor != null && neighbor.getId() != tile.getId()) {
                if ((bitmask & (1 << axial1)) == 0 && (bitmask & (1 << axial2)) == 0) {
                    bitmask |= (byte) (1 << cornerBit);     // set bit for corner
                }
            }
        }
        tile.setBitmask(bitmask);
    }
}
