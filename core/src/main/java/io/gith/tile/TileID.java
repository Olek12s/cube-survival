package io.gith.tile;

public enum TileID
{
    STONE(0, "Stone"),
    SAND(1, "Sand"),
    GRASS(2, "Grass"),
    DIRT(3, "Dirt"),
    GRAVEL(4, "Gravel"),
    WATER(5, "Water"),
    SNOW(6, "Snow");

    private final int value;
    private final String name;


    TileID(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {return value;}
    public String getTileName() {return name;}

    public static TileID fromName(String name) {
        for (TileID id : values()) {
            if (id.name.equalsIgnoreCase(name)) {
                return id;
            }
        }
        throw new IllegalArgumentException("Unknown tile name: " + name);
    }

    public static TileID fromValue(int value) {
        for (TileID id : values()) {
            if (id.value == value) return id;
        }
        throw new IllegalArgumentException("Unknown tile id: " + value);
    }

}
