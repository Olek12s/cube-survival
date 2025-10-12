package io.gith.tile;

import com.badlogic.gdx.math.Vector2;
import io.gith.*;
import io.gith.procedularGeneration.WorldGenerator;
import io.gith.procedularGeneration.overworld.Overworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RenderingOrder(Order.TILE)
public class TileMapController implements Renderable
{
    private final Map<Vector2, Chunk> loadedChunks;

    public void loadFirstChunks() {

        // TODO: TEMPORARY BLOCK

        WorldGenerator overworldGenerator = new Overworld(12);

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        ConcurrentHashMap<Vector2, Chunk> generatedChunks = new ConcurrentHashMap<>();
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            final int chunkX = i;
            for (int j = 0; j < 100; j++) {
                final int chunkY = j;
                tasks.add(() -> {
                    Chunk c = overworldGenerator.generateChunk(new Vector2(chunkX, chunkY));
                    generatedChunks.put(new Vector2(chunkX, chunkY), c);
                    return null;
                });
            }
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        for (Map.Entry<Vector2, Chunk> entry : generatedChunks.entrySet()) {
            putChunkOnMap(entry.getValue());
        }

        // TODO: TEMPORARY BLOCK

        // after loading all loadedChunks - assign bitmask
        for (Chunk chunk : loadedChunks.values()) {
            for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
                for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                    chunk.getTileLocalCoords(x, y).updateBitmask();
                }
            }
        }
    }


    public Map<Vector2, Chunk> getLoadedChunks() {
        return loadedChunks;
    }

    public TileMapController() {
        Main.getInstance().getRenderables().add(this);
        this.loadedChunks = new HashMap<>();
    }

    public Chunk getChunkFromMap(int chunkX, int chunkY) {
        Vector2 key = new Vector2(chunkX, chunkY);
        return loadedChunks.get(key);
    }

    public void putChunkOnMap(Chunk chunk) {
        Vector2 key = chunk.getChunkCoords();
        loadedChunks.putIfAbsent(key, chunk);
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
                chunk.setTileLocalCoords(tile, x, y);
            }
        }

        return chunk;
    }



    public Tile getTileAtIndex(int indexX, int indexY) {
        int chunkX = Math.floorDiv(indexX, Chunk.CHUNK_SIZE);
        int chunkY = Math.floorDiv(indexY, Chunk.CHUNK_SIZE);
        Chunk chunk = getChunkFromMap(chunkX, chunkY);
        if (chunk == null) {
            return null;
        }

        int localX = indexX - chunkX * Chunk.CHUNK_SIZE;
        int localY = indexY - chunkY * Chunk.CHUNK_SIZE;

        if (localX < 0 || localX >= Chunk.CHUNK_SIZE || localY < 0 || localY >= Chunk.CHUNK_SIZE) {
            return null;
        }

        return chunk.getTileLocalCoords(localX, localY);
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
            neighbors[i] = getTileAtIndex(nx, ny);
        }
        return neighbors;
    }


    @Override
    public void render() {
        CameraController cam = Main.getInstance().getCameraController();
        float chunkSizeInPix = Chunk.CHUNK_SIZE * CameraController.TILE_SIZE;


        for (Chunk c : loadedChunks.values()) {
            Vector2 pos = c.getChunkCoords();
            float baseX = pos.x * chunkSizeInPix;
            float baseY = pos.y * chunkSizeInPix;

            boolean visible = cam.isRectVisible(baseX, baseY, chunkSizeInPix, chunkSizeInPix);
            if (visible) {
                c.render();
            }
        }
    }
}
