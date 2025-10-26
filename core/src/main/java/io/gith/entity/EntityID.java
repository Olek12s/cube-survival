package io.gith.entity;

public enum EntityID
{
    PLAYER(0, "Player");

    private final int value;
    private final String name;


    EntityID(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {return value;}
    public String getEntityName() {return name;}

    public static EntityID fromName(String name) {
        for (EntityID id : values()) {
            if (id.name.equalsIgnoreCase(name)) {
                return id;
            }
        }
        throw new IllegalArgumentException("Unknown EntityID name: " + name);
    }

    public static EntityID fromValue(int value) {
        for (EntityID id : values()) {
            if (id.value == value) return id;
        }
        throw new IllegalArgumentException("Unknown EntityID id: " + value);
    }

}
