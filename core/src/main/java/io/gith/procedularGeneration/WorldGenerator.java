package io.gith.procedularGeneration;

import com.badlogic.gdx.math.Vector2;
import io.gith.tile.Chunk;

public interface WorldGenerator {
    Chunk generateChunk(int chunkX, int chunkY);
}
