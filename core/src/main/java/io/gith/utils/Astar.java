package io.gith.utils;

import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;

import java.util.*;

public class Astar extends Pathfinder
{

    @Override
    public ArrayList<Tile> findPathTiles(Tile start, Tile end, boolean skipCollidables, boolean allowPathThroughBlockedDiagonals)
    {
        TileMapController tileMap = Main.getInstance().getTileMap();

        HashMap<Tile, Tile> cameFrom = new HashMap<>();
        HashMap<Tile, Float> gScore = new HashMap<>();
        HashMap<Tile, Float> fScore = new HashMap<>();

        Comparator<Tile> comparator = Comparator.comparingDouble(
            t -> fScore.getOrDefault(t, Float.POSITIVE_INFINITY)
        );
        PriorityQueue<Tile> openSet = new PriorityQueue<>(comparator);
        HashSet<Tile> closedSet = new HashSet<>();

        // ----- COSTS INIT -----
        float gStart = 0f;
        float hStart = heuristic(start, end);
        float fStart = gStart + hStart;

        gScore.put(start, gStart);
        fScore.put(start, fStart);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Tile current = openSet.poll();  // retrieves and removes

            // found the target
            if (current.equals(end))
            {
                return reconstructPath(cameFrom, current);
            }
            closedSet.add(current);

            Tile[] neighbors = tileMap.getTile8NeighborsDiagLast(current);

            for (Tile neighbor : neighbors) {
                if (neighbor == null) continue;
                if (skipCollidables && neighbor.isCollidable()) continue;

                // Check diagonal blockage if needed
                if (!allowPathThroughBlockedDiagonals && isDiagonalMove(current, neighbor)) {
                    if (isDiagonalBlocked(tileMap, current, neighbor)) continue;
                }

                if (closedSet.contains(neighbor)) continue;

                // ---------- COST CALCULATION (g, h, f) ----------
                float gTentative = gScore.get(current) + distance(current, neighbor);
                float gCurrentBest = gScore.getOrDefault(neighbor, Float.POSITIVE_INFINITY);

                // if new cost is better - update
                if (gTentative < gCurrentBest) {

                    float h = heuristic(neighbor, end);
                    float f = gTentative + h;

                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, gTentative);
                    fScore.put(neighbor, f);

                    openSet.add(neighbor);
                }
            }
        }
        return new ArrayList<>();           // return if no path
    }


    private static float distance(Tile a, Tile b) {
        Vector2 pa = a.getIndexPosition();
        Vector2 pb = b.getIndexPosition();
        return pa.dst(pb);
    }
    private static float heuristic(Tile a, Tile b) {
        Vector2 pa = a.getIndexPosition();
        Vector2 pb = b.getIndexPosition();
        float distAB = pa.dst(pb);
        float distBA = pb.dst(pa);
        return (distAB + distBA) / 2f;
    }
    private static boolean isDiagonalMove(Tile a, Tile b) {
        int dx = (int) (b.getIndexPosition().x - a.getIndexPosition().x);
        int dy = (int) (b.getIndexPosition().y - a.getIndexPosition().y);
        return Math.abs(dx) == 1 && Math.abs(dy) == 1;
    }
}
