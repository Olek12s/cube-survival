package io.gith.procedularGeneration.overworld.biomes;

import com.badlogic.gdx.math.Vector2;
import io.gith.procedularGeneration.Biome;
import io.gith.tile.Tile;
import io.gith.tile.TileID;

public class TaigaBiome implements Biome
{
    @Override
    public Tile generateTile(int worldPositionX, int worldPositionY, int seed) {
        return new Tile.Builder()
            .id(TileID.SNOW)
            .position(new Vector2(
                worldPositionX,
                worldPositionY))
            .build();
    }
}
