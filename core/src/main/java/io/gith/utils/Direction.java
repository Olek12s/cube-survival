package io.gith.utils;

import com.badlogic.gdx.math.Vector2;

public enum Direction
{
    UP,
    DOWN,
    LEFT,
    RIGHT;



    public static Direction fromVector(Vector2 vec) {
        if (vec.isZero()) return DOWN;

        if (Math.abs(vec.x) > Math.abs(vec.y)) {
            return vec.x > 0 ? RIGHT : LEFT;
        } else {
            return vec.y > 0 ? UP : DOWN;
        }
    }
}

