package io.gith.tile;

import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.utils.Pair;

import java.util.HashMap;
import java.util.Map;


public class Autotiler
{
    private static final Map<Integer, Pair<Integer, Integer>> maskToVariant = new HashMap<>();


    /**
     *
     * @param tile   target tile
     */
    public static void assignBitmask(Tile tile) {
        Tile[] neighboringTiles = Main.getInstance().getTileMap().getTileNeighbors(tile);
     //   System.out.println("assigning bitMask for Tile: " + tile.getWorldPosition() + " Neighbors: ");

        System.out.println(">>>>>tile: " + tile.getIndexPosition());
        for (Tile n : neighboringTiles) {
            if (n == null) System.out.println("null");
            else System.out.println(": " + n.getIndexPosition());
        }
    }
}
