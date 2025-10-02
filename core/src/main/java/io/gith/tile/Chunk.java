package io.gith.tile;

import com.badlogic.gdx.math.Vector2;

import static io.gith.CameraController.TILE_SIZE;

public class Chunk {
    public static final int CHUNK_SIZE = 8;
    private Tile[][] tiles;
    private Vector2 position;

    public Chunk(Vector2 position) {
        tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
        this.position = position;
    }

    public void setTile(Tile tile, int x, int y) {
        tiles[x][y] = tile;
    }
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
    public int getX() {
        return (int)position.x * CHUNK_SIZE;
    }
    public int getY() {
        return (int)position.y * CHUNK_SIZE;
    }
    public Vector2 getPosition() {return position;}
    public Tile[][] getTiles() {
        return tiles;
    }
    public Vector2 getWorldPosition() {return new Vector2(getWorldX(), getWorldY());}
    public int getWorldX() {return (int)(getX() * TILE_SIZE);}
    public int getWorldY() {return (int)(getY() * TILE_SIZE);}


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
