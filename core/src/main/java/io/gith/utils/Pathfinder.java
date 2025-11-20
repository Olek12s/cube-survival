package io.gith.utils;

import io.gith.tile.Tile;
import io.gith.tile.TileMapController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class Pathfinder
{
    public abstract ArrayList<Tile> findPathTiles(Tile start, Tile end, boolean skipCollidables, boolean allowPathThroughBlockedDiagonals);

    protected static boolean isDiagonalBlocked(TileMapController tileMap, Tile from, Tile to)
    {
        int dx = (int) (to.getIndexPosition().x - from.getIndexPosition().x);
        int dy = (int) (to.getIndexPosition().y - from.getIndexPosition().y);

        // For example, moving from (x, y) → (x+1, y+1)
        // Check (x+1, y) and (x, y+1)
        Tile neighbor1 = tileMap.getTileAtIndex(
            (int) from.getIndexPosition().x + dx,
            (int) from.getIndexPosition().y
        );
        Tile neighbor2 = tileMap.getTileAtIndex(
            (int) from.getIndexPosition().x,
            (int) from.getIndexPosition().y + dy
        );

        return (neighbor1 != null && neighbor1.isCollidable()) ||
            (neighbor2 != null && neighbor2.isCollidable());
    }

    protected static ArrayList<Tile> reconstructPath(HashMap<Tile, Tile> cameFrom, Tile current) {
        ArrayList<Tile> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current))
        {
            current = cameFrom.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }
}
