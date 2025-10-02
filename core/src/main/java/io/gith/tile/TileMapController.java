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
        Vector2 key = new Vector2(chunk.getWorldX(), chunk.getWorldY());
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
        int worldTileX = (int)(tile.getPosition().x);
        int worldTileY = (int)(tile.getPosition().y);

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




    @Override
    public void render() {
        for (Chunk c : chunks.values()) {
            c.render();
        }
    }
}
