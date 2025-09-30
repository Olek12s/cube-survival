package io.gith.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Chunk {
    public static final int CHUNK_SIZE = 16;
    private Tile[][] tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
    private int chunkX, chunkY;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWorldX() {
        return chunkX * CHUNK_SIZE;
    }

    public int getWorldY() {
        return chunkY * CHUNK_SIZE;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
