package io.gith.utils;


import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.tile.Chunk;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;

import java.util.*;

public class BFS {
    public static ArrayList<Tile> findPathTiles(Tile start, Tile end, boolean skipCollidables) {
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
            Tile[] neighbors = tileMap.getTileNeighbors(current);
            for (Tile neighbor : neighbors) {
                if (neighbor == null) continue;
                if (neighbor.isCollidable() && skipCollidables) continue;  // skip collidable tiles

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
}
