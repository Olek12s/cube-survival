package io.gith.tile;

import com.badlogic.gdx.math.Vector2;
import io.gith.*;

import java.util.HashMap;
import java.util.Map;

@RenderingOrder(Order.TILE)
public class TileMapController implements Renderable
{
    private final Map<Vector2, Chunk> chunks;

    public TileMapController() {
        Main.getInstance().getRenderables().add(this);
        this.chunks = new HashMap<>();
    }

    public Chunk getChunkFromMap(int chunkX, int chunkY) {
        Vector2 key = new Vector2(chunkX, chunkY);
        return chunks.get(key);
    }

    public void putChunkOnMap(Chunk chunk) {
        Vector2 key = new Vector2(chunk.getIndexX(), chunk.getIndexY());
        chunks.put(key, chunk);
    }

    public static Chunk createChunkFromIDs(short[][] tileIDs, Vector2 chunkPos) {
        Chunk chunk = new Chunk(chunkPos);
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                Tile tile = new Tile.Builder()
                    .id(TileID.fromValue(tileIDs[x][y]))
                    .position(new Vector2(
                        (chunkPos.x * Chunk.CHUNK_SIZE + x),
                        (chunkPos.y * Chunk.CHUNK_SIZE + y)
                    ))
                    .collidable(false)
                    .build();
                chunk.setTile(tile, x, y);
            }
        }
        return chunk;
    }

    public void replaceOrSetTile(Tile tile) {
        int worldTileX = (int)(tile.getIndexPosition().x);
        int worldTileY = (int)(tile.getIndexPosition().y);

        int chunkX = worldTileX / Chunk.CHUNK_SIZE;
        int chunkY = worldTileY / Chunk.CHUNK_SIZE;

        int localX = worldTileX % Chunk.CHUNK_SIZE;
        int localY = worldTileY % Chunk.CHUNK_SIZE;


        Chunk chunk = getChunkFromMap(chunkX, chunkY);
        /*
        if (chunk == null) {
            putChunkOnMap(chunkX, chunkY);
            chunk = getChunkFromMap(chunkX, chunkY);
        }
        */
        chunk.setTile(tile, localX, localY);
    }

    public Tile getTileAtWorld(int worldTileX, int worldTileY) {
        int chunkX = (int) Math.floor((float) worldTileX / Chunk.CHUNK_SIZE);
        int chunkY = (int) Math.floor((float) worldTileY / Chunk.CHUNK_SIZE);

        Chunk chunk = getChunkFromMap(chunkX, chunkY);
        if (chunk == null) {
            return null;
        }

        int localX = Math.floorMod(worldTileX, Chunk.CHUNK_SIZE);
        int localY = Math.floorMod(worldTileY, Chunk.CHUNK_SIZE);

        return chunk.getTile(localX, localY);
    }

    public Tile[] getTileNeighbors(Tile tile) {
        Tile[] neighbors = new Tile[8];

        int x = (int) tile.getIndexPosition().x;
        int y = (int) tile.getIndexPosition().y;

        int[][] offsets = {
            {0, 1},  // N
            {1, 1},  // NE
            {1, 0},  // E
            {1, -1}, // SE
            {0, -1}, // S
            {-1, -1},// SW
            {-1, 0}, // W
            {-1, 1}  // NW
        };

        for (int i = 0; i < offsets.length; i++) {
            int nx = x + offsets[i][0];
            int ny = y + offsets[i][1];
            neighbors[i] = getTileAtWorld(nx, ny);
        }
        return neighbors;
    }




    @Override
    public void render() {
        for (Chunk c : chunks.values()) {
            c.render();
        }
    }
}
