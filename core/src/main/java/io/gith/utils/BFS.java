package io.gith.utils;


import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.tile.Chunk;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;

import java.util.*;

public class BFS extends Pathfinder {
    public ArrayList<Tile> findPathTiles(Tile start, Tile end, boolean skipCollidables, boolean allowPathThroughBlockedDiagonals) {
        TileMapController tileMap = Main.getInstance().getTileMap();

        HashMap<Tile, Boolean> visited = new HashMap<>();
        HashMap<Tile, Tile> cameFrom = new HashMap<>();
        Queue<Tile> queue = new LinkedList<>();

        visited.put(start, true);
        queue.add(start);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            // found target
            if (current.equals(end))
            {
                return reconstructPath(cameFrom, end);
            }

            // visit all unvisited neighbors of current node
            Tile[] neighbors = tileMap.getTile8NeighborsDiagLast(current);
            for (Tile neighbor : neighbors) {
                if (neighbor == null) continue;
                if (neighbor.isCollidable() && skipCollidables) continue;  // skip collidable tiles

                // Check diagonal blockage if needed
                if (!allowPathThroughBlockedDiagonals && isDiagonalMove(current, neighbor)) {
                    if (Pathfinder.isDiagonalBlocked(tileMap, current, neighbor)) continue;
                }

                if (!visited.containsKey(neighbor)) {
                    cameFrom.put(neighbor, current);
                    visited.put(neighbor, true);
                    queue.add(neighbor);
                }
            }
        }
        return new ArrayList<>();   // no target found - empty list
    }

    private static boolean isDiagonalMove(Tile a, Tile b) {
        int dx = (int) (b.getIndexPosition().x - a.getIndexPosition().x);
        int dy = (int) (b.getIndexPosition().y - a.getIndexPosition().y);
        return Math.abs(dx) == 1 && Math.abs(dy) == 1;
    }
}
