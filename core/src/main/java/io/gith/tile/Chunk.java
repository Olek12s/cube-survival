package io.gith.tile;

import com.badlogic.gdx.math.Vector2;

import static io.gith.CameraController.TILE_SIZE;

public class Chunk {
    public static final int CHUNK_SIZE = 8;
    private Tile[][] tiles;
    private Vector2 indexPosition;
    private Vector2 chunkCoords;

    public Chunk(Vector2 chunkCoords) {
        this.chunkCoords = chunkCoords;
        this.indexPosition = new Vector2(chunkCoords.x * CHUNK_SIZE, chunkCoords.y * CHUNK_SIZE);
        this.tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
    }

    public void setTileLocalCoords(Tile tile, int x, int y) {
        tiles[x][y] = tile;
    }
    public Tile getTileLocalCoords(int x, int y) {
        return tiles[x][y];
    }
    public Vector2 getIndexPosition() {return indexPosition;}
    public Tile[][] getTiles() {
        return tiles;
    }
    public Tile getTile(int localX, int localY) {return tiles[localX][localY];}
    public void setTile(int localX, int localY, Tile tile) {
        tiles[localX][localY] = tile;
    }
    public Vector2 getWorldPosition() {return new Vector2(getWorldX(), getWorldY());}
    public int getIndexX() {
        return (int) indexPosition.x * CHUNK_SIZE;
    }
    public int getIndexY() {
        return (int) indexPosition.y * CHUNK_SIZE;
    }
    public int getWorldX() {return (int)(getIndexX() * TILE_SIZE);}
    public int getWorldY() {return (int)(getIndexY() * TILE_SIZE);}
    public Vector2 getChunkCoords() {return chunkCoords;}


    protected void render()
    {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y] == null) continue;  // don't try to render null tile
                tiles[x][y].render();
            }
        }
    }
}
