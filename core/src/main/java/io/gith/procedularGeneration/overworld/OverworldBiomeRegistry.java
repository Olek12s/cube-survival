package io.gith.procedularGeneration.overworld;

import io.gith.procedularGeneration.Biome;
import io.gith.procedularGeneration.BiomeName;
import io.gith.procedularGeneration.overworld.biomes.*;

import java.util.HashMap;
import java.util.Map;

public class OverworldBiomeRegistry {
    private final Map<BiomeName, Biome> biomeMap;

    public Biome get(BiomeName biomeName) {
        return biomeMap.get(biomeName);
    }

    public OverworldBiomeRegistry() {
        biomeMap = new HashMap<>();

        biomeMap.put(BiomeName.Plains, new PlainsBiome());
        biomeMap.put(BiomeName.Desert, new DesertBiome());
        biomeMap.put(BiomeName.Mountains, new MountainsBiome());
        biomeMap.put(BiomeName.Highlands, new HighlandsBiome());
        biomeMap.put(BiomeName.Taiga, new TaigaBiome());
        biomeMap.put(BiomeName.Ocean, new OceanBiome());
        biomeMap.put(BiomeName.Coast, new CoastBiome());
        biomeMap.put(BiomeName.Forest, new ForestBiome());
    }
}
