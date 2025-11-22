package io.gith.gui;

public enum Scale
{
    SMALL(1.5f),
    MEDIUM(2f),
    LARGE(3);

    private final float scale;

    public float getValue() {
        return scale;
    }

    Scale(float scale) {
        this.scale = scale;
    }
}
