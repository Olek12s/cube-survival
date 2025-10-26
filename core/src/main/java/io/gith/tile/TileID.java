package io.gith.tile;

public enum TileID
{
    STONE(0, "Stone", true),
    SAND(1, "Sand", false),
    GRASS(2, "Grass", false),
    DIRT(3, "Dirt", false),
    GRAVEL(4, "Gravel", false),
    WATER(5, "Water", true),    // TODO - water is collidable temporarly
    SNOW(6, "Snow", false);

    private final int value;
    private final String name;
    private final boolean collidable;

    TileID(int value, String name, boolean collidable) {
        this.value = value;
        this.name = name;
        this.collidable = collidable;
    }

    public int getValue() {return value;}
    public String getTileName() {return name;}
    public boolean isCollidable() { return collidable; }

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
