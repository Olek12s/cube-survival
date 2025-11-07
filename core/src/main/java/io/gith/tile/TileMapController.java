package io.gith.tile;

import com.badlogic.gdx.math.Vector2;
import io.gith.*;
import io.gith.procedularGeneration.WorldGenerator;
import io.gith.procedularGeneration.overworld.Overworld;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RenderingOrder(Order.TILE)
public class TileMapController implements Renderable, Updatable
{
    @Getter
    private final Map<Long, Chunk> loadedChunks;
    private final int CHUNK_LOAD_RANGE = 8;
    private final ExecutorService executor;
    private final WorldGenerator worldGen;
    private Vector2 lastCameraChunk;

    public TileMapController() {
        this.loadedChunks = new ConcurrentHashMap<>();
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.worldGen = new Overworld((int) System.currentTimeMillis());    // TODO: strategy pattern
        Main.getInstance().getRenderables().add(this);
        Main.getInstance().getUpdatables().add(this);
        lastCameraChunk = new Vector2();
    }

    // pack int x,y into single long key
    private static long keyFor(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }

    // helper: unpack (if needed)
    private static int keyToX(long key) { return (int) (key >> 32); }
    private static int keyToY(long key) { return (int) key; }

    @Override
    public void update(float dt) {
        updateLoadedChunks();
    }

    private void updateLoadedChunks() {
        Vector2 cameraPosition = Main.getInstance().getCameraController().getTargetPosition();

        int playerChunkX = (int) Math.floor(cameraPosition.x / (Chunk.CHUNK_SIZE * CameraController.TILE_SIZE));
        int playerChunkY = (int) Math.floor(cameraPosition.y / (Chunk.CHUNK_SIZE * CameraController.TILE_SIZE));
        Vector2 currentChunk = new Vector2(playerChunkX, playerChunkY);

        // if chunk was not changed - no update
        if (currentChunk.equals(lastCameraChunk) && !loadedChunks.isEmpty()) return;
        lastCameraChunk.set(currentChunk);

        Set<Long> requiredChunks = new HashSet<>();

        // select required chunk positions to load
        for (int dx = -CHUNK_LOAD_RANGE; dx <= CHUNK_LOAD_RANGE; dx++) {
            for (int dy = -CHUNK_LOAD_RANGE; dy <= CHUNK_LOAD_RANGE; dy++) {
                requiredChunks.add(keyFor(playerChunkX + dx, playerChunkY + dy));
            }
        }

        // remove chunks out of range
        // loadedChunks.keySet().removeIf(k -> !requiredChunks.contains(k));

        // load required chunks (async)
        for (Long k : requiredChunks) {
            if (!loadedChunks.containsKey(k)) {
                final int cx = keyToX(k);
                final int cy = keyToY(k);

                executor.submit(() -> {
                    Chunk chunk = worldGen.generateChunk(cx, cy);   // generate chunk

                    // insert chunk into map and calculate bitmasks for it and its neighbours
                    synchronized (loadedChunks) {
                        loadedChunks.putIfAbsent(keyFor(cx, cy), chunk);
                        recalcBitmasksForChunk(chunk);

                        // recalculate neighbors bitmasks
                        for (int sx = -1; sx <= 1; sx++) {
                            for (int sy = -1; sy <= 1; sy++) {
                                if (sx == 0 && sy == 0) continue;   // self
                                long neighborKey = keyFor(cx + sx, cy + sy);
                                Chunk neighbor = loadedChunks.get(neighborKey);
                                if (neighbor != null) {
                                    recalcBitmasksForChunk(neighbor);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private void recalcBitmasksForChunk(Chunk chunk) {
        if (chunk == null) return;
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {    // recalculate bitnask for every Tile in chunk
            for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
                Tile t = chunk.getTileLocalCoords(x, y);
                if (t != null) {
                    t.updateBitmask();
                }
            }
        }
    }


    public Chunk getChunkFromMap(int chunkX, int chunkY) {
        Long key = keyFor(chunkX, chunkY);
        return loadedChunks.get(key);
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
    public Tile getTileAtWorldPosition(float worldX, float worldY) {
        int tileX = Math.floorDiv((int) worldX, (int) CameraController.TILE_SIZE);
        int tileY = Math.floorDiv((int) worldY, (int) CameraController.TILE_SIZE);

        return getTileAtIndex(tileX, tileY);
    }

    public Tile getTileAtWorldPosition(Vector2 pos) {
        float worldX = pos.x;
        float worldY = pos.y;

        int tileX = Math.floorDiv((int) worldX, (int) CameraController.TILE_SIZE);
        int tileY = Math.floorDiv((int) worldY, (int) CameraController.TILE_SIZE);

        return getTileAtIndex(tileX, tileY);
    }
    public Tile[] getTile8Neighbors(Tile tile) {
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

    public Tile[] getTile8NeighborsDiagLast(Tile tile) {
        Tile[] neighbors = new Tile[8];


        int x = (int) tile.getIndexPosition().x;
        int y = (int) tile.getIndexPosition().y;

        int[][] offsets = {
            {0, 1},   // N
            {1, 0},   // E
            {0, -1},  // S
            {-1, 0},  // W
            {1, 1},   // NE
            {1, -1},  // SE
            {-1, -1}, // SW
            {-1, 1}   // NW
        };

        for (int i = 0; i < offsets.length; i++) {
            int nx = x + offsets[i][0];
            int ny = y + offsets[i][1];
            neighbors[i] = getTileAtIndex(nx, ny);
        }
        return neighbors;
    }

    public Tile[] getTile4Neighbors(Tile tile) {
        Tile[] neighbors = new Tile[4];


        int x = (int) tile.getIndexPosition().x;
        int y = (int) tile.getIndexPosition().y;

        int[][] offsets = {
            {0, 1},  // N
            {1, 0},  // E
            {0, -1}, // S
            {-1, 0}, // W
        };

        for (int i = 0; i < offsets.length; i++) {
            int nx = x + offsets[i][0];
            int ny = y + offsets[i][1];
            neighbors[i] = getTileAtIndex(nx, ny);
        }
        return neighbors;
    }

    // TODO: MapRenderer class
    @Override
    public void renderTexture() {
        CameraController cam = Main.getInstance().getCameraController();
        float chunkSizeInPix = Chunk.CHUNK_SIZE * CameraController.TILE_SIZE;


        for (Chunk c : loadedChunks.values()) {
            Vector2 pos = c.getChunkCoords();
            float baseX = pos.x * chunkSizeInPix;
            float baseY = pos.y * chunkSizeInPix;

            boolean visible = cam.isRectVisible(baseX, baseY, chunkSizeInPix, chunkSizeInPix);
            if (visible) {
                c.renderTextures();
            }
        }
    }

    @Override
    public void renderShape() {
        CameraController cam = Main.getInstance().getCameraController();
        float chunkSizeInPix = Chunk.CHUNK_SIZE * CameraController.TILE_SIZE;


        for (Chunk c : loadedChunks.values()) {
            Vector2 pos = c.getChunkCoords();
            float baseX = pos.x * chunkSizeInPix;
            float baseY = pos.y * chunkSizeInPix;

            boolean visible = cam.isRectVisible(baseX, baseY, chunkSizeInPix, chunkSizeInPix);
            if (visible) {
                c.renderOutlinesChunk();
                c.renderOutlinesEachTile();
            }
        }
    }
}
