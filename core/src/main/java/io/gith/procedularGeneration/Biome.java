package io.gith.procedularGeneration;

import io.gith.tile.Tile;

public interface Biome
{
    Tile generateTile(int worldX, int worldY, int seed);
}
