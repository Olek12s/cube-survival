package io.gith.tile;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.entity.entity.Entity;

import java.util.ArrayList;

import static io.gith.CameraController.TILE_SIZE;

public class Chunk {
    public static final int CHUNK_SIZE = 8;
    private Tile[][] tiles;
    private Vector2 indexPosition;
    private Vector2 chunkCoords;
    private ArrayList<Entity> entities;

    public Chunk(Vector2 chunkCoords) {
        this.chunkCoords = chunkCoords;
        this.indexPosition = new Vector2(chunkCoords.x * CHUNK_SIZE, chunkCoords.y * CHUNK_SIZE);
        this.tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
        this.entities = new ArrayList<>();
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
    public ArrayList<Entity> getEntities() {return entities;}
    public Vector2 getChunkCoords() {return chunkCoords;}

    public boolean isWithinChunk(Vector2 worldPos) {
        return isWithinChunk(worldPos.x, worldPos.y);
    }

    public boolean isWithinChunk(float worldX, float worldY) {
        float chunkWorldX = indexPosition.x * TILE_SIZE;
        float chunkWorldY = indexPosition.y * TILE_SIZE;
        float chunkSizeInPixels = CHUNK_SIZE * TILE_SIZE;

        return worldX >= chunkWorldX && worldX < chunkWorldX + chunkSizeInPixels
            && worldY >= chunkWorldY && worldY < chunkWorldY + chunkSizeInPixels;
    }



    protected void renderTextures()
    {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y] == null) continue;  // don't try to render null tile
                tiles[x][y].render();
            }
        }
    }

    protected void renderOutlinesChunk()
    {
        ShapeRenderer shapeRenderer = Main.getInstance().getShapeRenderer();

        float x = indexPosition.x * TILE_SIZE;
        float y = indexPosition.y * TILE_SIZE;
        float width = CHUNK_SIZE * TILE_SIZE;
        float height = CHUNK_SIZE * TILE_SIZE;

        shapeRenderer.setColor(0, 1, 1, 1f);
        shapeRenderer.rect(x, y, width, height);
    }

    protected void renderOutlinesEachTile()
    {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (tiles[x][y] == null) continue;
                tiles[x][y].renderOutline();
            }
        }
    }

    @Override
    public String toString() {
        return "Chunk: " + chunkCoords;
    }
}
