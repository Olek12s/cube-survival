package io.gith.procedularGeneration.biome.overworld;

import com.badlogic.gdx.math.Vector2;
import io.gith.procedularGeneration.WorldGenerator;
import io.gith.procedularGeneration.biome.Biome;
import io.gith.tile.Chunk;
import io.gith.tile.Tile;

public class Overworld implements WorldGenerator
{
    private int seed;

    private OverworldBiomeGenerator biomeGenerator;
    private OverworldBiomeRegistry biomeRegistry;

    public Overworld(int seed) {
        this.seed = seed;
        biomeGenerator = new OverworldBiomeGenerator(seed);
        biomeRegistry = new OverworldBiomeRegistry();
    }

    @Override
    public Chunk generateChunk(Vector2 chunkCoords) {
        Chunk chunk = new Chunk(chunkCoords);

        double worldStartX = chunkCoords.x * Chunk.CHUNK_SIZE;
        double worldStartY = chunkCoords.y * Chunk.CHUNK_SIZE;

        for (int localX = 0; localX < Chunk.CHUNK_SIZE; localX++) {
            for (int localY = 0; localY < Chunk.CHUNK_SIZE; localY++) {
                int worldX = (int)(worldStartX + localX);
                int worldY = (int)(worldStartY + localY);

                Biome biome = biomeGenerator.generateBiome(worldX, worldY);
                OverworldBiome biomeLogic = biomeRegistry.get(biome);

                Tile tile = biomeLogic.generateTile(worldX, worldY, seed);
                chunk.setTile(localX, localY, tile);
            }
        }

        return chunk;
    }
}
