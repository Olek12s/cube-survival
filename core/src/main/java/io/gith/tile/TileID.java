package io.gith.tile;

public enum TileID
{
    GRASS(0, "grass"),
    STONE(1, "stone"),
    SAND(2, "sand");

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
