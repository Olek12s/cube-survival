package io.gith.utils;


import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.tile.Chunk;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;

import java.util.*;

public class BFS {
    public static ArrayList<Tile> findPathTiles(Tile start, Tile end, boolean skipCollidables, boolean allowPathThroughBlockedDiagonals) {
        TileMapController tileMap = Main.getInstance().getTileMap();

        HashMap<Tile, Boolean> visited = new HashMap<>();
        HashMap<Tile, Tile> cameFrom = new HashMap<>();
        Queue<Tile> queue = new LinkedList<>();

        visited.put(start, true);
        queue.add(start);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            if (current.equals(end)) {  // found target
                break;
            }

            // visit all unvisited neighbors of current node
            Tile[] neighbors = tileMap.getTile8NeighborsDiagLast(current);
            for (Tile neighbor : neighbors) {
                if (neighbor == null) continue;
                if (neighbor.isCollidable() && skipCollidables) continue;  // skip collidable tiles

                // Check diagonal blockage if needed
                if (!allowPathThroughBlockedDiagonals && isDiagonalMove(current, neighbor)) {
                    if (isDiagonalBlocked(tileMap, current, neighbor)) continue;
                }

                if (!visited.containsKey(neighbor)) {
                    cameFrom.put(neighbor, current);
                    visited.put(neighbor, true);
                    queue.add(neighbor);
                }
            }
        }

        // reconstruct path
        ArrayList<Tile> path = new ArrayList<>();
        Tile step = end;

        while (step != null) {
            path.add(step);
            step = cameFrom.get(step);
        }

        Collections.reverse(path);
        return path;
    }

    private static boolean isDiagonalMove(Tile a, Tile b) {
        int dx = (int) (b.getIndexPosition().x - a.getIndexPosition().x);
        int dy = (int) (b.getIndexPosition().y - a.getIndexPosition().y);
        return Math.abs(dx) == 1 && Math.abs(dy) == 1;
    }

    private static boolean isDiagonalBlocked(TileMapController tileMap, Tile from, Tile to) {
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
}
