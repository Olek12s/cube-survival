package io.gith.utils;

import com.badlogic.gdx.math.Vector2;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar
{
    private static class Node {
        Vector2 position;
        Node parent;
        float f;    // = g + h. A* picks Node with lowest f value
        float g;    // movement cost from start to a given node
        float h;    // movement cost from given node to end

        protected Node(Vector2 position) {
            this.position = position;
        }

        protected Node(Vector2 position, Node parent) {
            this.position = position;
            this.parent = parent;
        }
    }


    public static ArrayList<Vector2> findPathTile(Vector2 startPos, Vector2 endPos, TileMapController map)
    {
        Comparator<Node> comparator = Comparator.comparingDouble(n -> n.f);
        PriorityQueue<Node> openList = new PriorityQueue<>(comparator);
        ArrayList<Node> closedList = new ArrayList<>();

        Node startNode = new Node(startPos); // add starting node to the open list
        startNode.g = 0;
        startNode.h = heuristic(startPos, endPos);
        startNode.f = startNode.g + startNode.h;
        openList.add(startNode);

        // while open list is not empty
        while (!openList.isEmpty()) {
            // find the node with smallest f-cost
            Node current = openList.poll();
            closedList.add(current);

            // Check if goal reached
            if (current.position.equals(endPos)) {
                return reconstructPath(current);
            }

            // generate q's 8 successors and set their parents to q
            for (int[] dir : Direction.DIRS8) {
                Vector2 neighborPos = new Vector2(current.position.x + dir[0], current.position.y + dir[1]);
                Tile neighborTile = map.getTileAtIndex((int) neighborPos.x, (int) neighborPos.y);
                if (neighborTile == null || neighborTile.isCollidable()) continue;

                Node neighborNode = new Node(neighborPos, current);
                neighborNode.g = current.g + current.position.dst(neighborPos);
                neighborNode.h = heuristic(neighborPos, endPos);
                neighborNode.f = neighborNode.g + neighborNode.h;

                // Skip if better node already in open list
                boolean skip = false;
                for (Node n : openList) {
                    if (n.position.equals(neighborPos) && n.f <= neighborNode.f) {
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;

                // Skip if better node already in closed list
                for (Node n : closedList) {
                    if (n.position.equals(neighborPos) && n.f <= neighborNode.f) {
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;

                openList.add(neighborNode);
            }
        }

        return new ArrayList<>(); // no path found
    }

    private static ArrayList<Vector2> reconstructPath(Node node) {
        ArrayList<Vector2> path = new ArrayList<>();
        Node current = node;
        while (current != null) {
            path.add(current.position);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }


    private static float heuristic(Vector2 a, Vector2 b) {
        return a.dst(b);    // Using Euclidean distance
    }
}
