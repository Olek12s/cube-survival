package io.gith.procedularGeneration.overworld;

import io.gith.procedularGeneration.Perlin;
import io.gith.procedularGeneration.BiomeName;

public class OverworldBiomeGenerator
{
    private final Perlin temperatureNoise;
    private final Perlin humidityNoise;
    private final Perlin continentNoise;
    private final Perlin heightNoise;
    private static final float scale = 0.0085f;  // smaller number - larger biomes   reccomended: 0.007f (large biomes) - 0.01f (small biomes)

    public OverworldBiomeGenerator(int seed) {
        this.temperatureNoise = new Perlin(seed, 4, 0.5f, 2.0f);
        this.humidityNoise = new Perlin(seed+1, 2, 0.5f, 2.0f);
        this.continentNoise = new Perlin(seed+2, 2, 0.5f, 2.0f);
        this.heightNoise = new Perlin(seed+3, 2, 0.5f, 2.0f);
    }

    public BiomeName generateBiome(double worldX, double worldY) {
        double x = worldX * scale;
        double y = worldY * scale;

        double temperature = temperatureNoise.perlinNoise(x, y);
        double humidity = humidityNoise.perlinNoise(x, y);
        double continent = continentNoise.perlinNoise(x, y);
        double height = heightNoise.perlinNoise(x, y);

        //  Ocean / Coast
        if (continent < 0.45) return BiomeName.Ocean;
        if (continent < 0.47) return BiomeName.Coast;

        //  Mountains / Highlands
        if (height > 0.75) return BiomeName.Mountains;
        if (height > 0.65) return BiomeName.Highlands;

        //  Desert
        if (height <= 0.65 && temperature > 0.5 && humidity < 0.45) return BiomeName.Desert;

        //  Taiga
        if (temperature < 0.4 && humidity > 0.45) return BiomeName.Taiga;

        //  Forest
        if (temperature > 0.45 && humidity > 0.5) return BiomeName.Forest;

        //  Plains
        return BiomeName.Plains;
    }
}
