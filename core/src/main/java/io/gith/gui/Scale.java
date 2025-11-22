package io.gith.gui;

public enum Scale
{
    SMALL(1),
    MEDIUM(1.5f),
    LARGE(2);

    private final float scale;

    public float getValue() {
        return scale;
    }

    Scale(float scale) {
        this.scale = scale;
    }
}
