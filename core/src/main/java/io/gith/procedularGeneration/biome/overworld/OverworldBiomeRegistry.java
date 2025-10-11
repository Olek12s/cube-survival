package io.gith.procedularGeneration.biome.overworld;

import io.gith.procedularGeneration.WorldGenerator;
import io.gith.procedularGeneration.biome.Biome;
import io.gith.procedularGeneration.biome.overworld.biomes.*;

import java.util.HashMap;
import java.util.Map;

public class OverworldBiomeRegistry {
    private final Map<Biome, OverworldBiome> biomeMap;

    public OverworldBiome get(Biome biome) {
        return biomeMap.get(biome);
    }

    public OverworldBiomeRegistry() {
        biomeMap = new HashMap<>();

        biomeMap.put(Biome.Plains, new PlainsBiome());
        biomeMap.put(Biome.Desert, new DesertBiome());
        biomeMap.put(Biome.Mountains, new MountainsBiome());
        biomeMap.put(Biome.Highlands, new HighlandsBiome());
        biomeMap.put(Biome.Taiga, new TaigaBiome());
        biomeMap.put(Biome.Ocean, new OceanBiome());
        biomeMap.put(Biome.Coast, new CoastBiome());
        biomeMap.put(Biome.Forest, new ForestBiome());
    }
}
