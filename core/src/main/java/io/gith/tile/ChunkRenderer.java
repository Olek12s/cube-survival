package io.gith.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ChunkRenderer {
    private Chunk chunk;

    public ChunkRenderer(Chunk chunk) {
        this.chunk = chunk;
    }

    public void render(SpriteBatch batch) {
        Tile[][] tiles = chunk.getTiles();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                Tile tile = tiles[x][y];
                if (tile != null) {
                    batch.draw(tile.getTextureRegion("1"), chunk.getWorldX() + x, chunk.getWorldY() + y);
                }
            }
        }
    }
}
