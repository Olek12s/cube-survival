package io.gith.procedularGeneration.biome.overworld;

import io.gith.tile.Tile;

public interface OverworldBiome
{
    Tile generateTile(int worldX, int worldY, int seed);
}
