package io.gith.procedularGeneration;

import java.util.Random;

public final class Perlin
{
    private int seed;
    private int octaves;
    private double persistence;
    private double lacunarity;

    public Perlin(int seed, int octaves, double persistence, double lacunarity) {
        this.seed = seed;
        this.octaves = octaves;
        this.persistence = persistence;
        this.lacunarity = lacunarity;
    }

    public double perlinNoise(double pixelX, double pixelY)
    {
        int[] p = buildPermutation(seed);

        double total = 0.0;
        double frequency = 1.0;
        double amplitude = 1.0;
        double maxAmplitude = 0.0;

        for (int i = 0; i < Math.max(1, octaves); i++) {
            double nx = pixelX * frequency;
            double ny = pixelY * frequency;
            double noise = perlin2(nx, ny, p);
            total += noise * amplitude;

            maxAmplitude += amplitude;
            amplitude *= persistence;
            frequency *= lacunarity;
        }

        double normalized = (total / maxAmplitude + 1.0) * 0.5;
        return Math.min(Math.max(normalized, 0.0), 1.0);
    }


    private static int[] buildPermutation(int seed) {
        int[] perm = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) p[i] = i;

        Random rng = new Random(seed);
        for (int i = 255; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }

        for (int i = 0; i < 512; i++) perm[i] = p[i & 255];
        return perm;
    }

    private static double perlin2(double x, double y, int[] perm) {
        int xi = (int)Math.floor(x) & 255;
        int yi = (int)Math.floor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa = perm[xi + perm[yi]];
        int ab = perm[xi + perm[yi + 1]];
        int ba = perm[xi + 1 + perm[yi]];
        int bb = perm[xi + 1 + perm[yi + 1]];

        double x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
        double x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);
        return lerp(x1, x2, v);
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x, double y) {
        int h = hash & 7; // 8 directions of gradient
        switch (h) {
            case 0: return x + y;
            case 1: return -x + y;
            case 2: return x - y;
            case 3: return -x - y;
            case 4: return x;
            case 5: return -x;
            case 6: return y;
            case 7: return -y;
            default: return 0;
        }
    }
}
