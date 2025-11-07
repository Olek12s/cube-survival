package io.gith.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static Vector2 directionToVector(Direction dir) {
        switch (dir) {
            case UP: return new Vector2(0, 1);
            case DOWN: return new Vector2(0, -1);
            case LEFT: return new Vector2(-1, 0);
            case RIGHT: return new Vector2(1, 0);
            default: return new Vector2(0, 0);
        }
    }


    public static final int[][] DIRS8 = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},           {0, 1},
        {1, -1},  {1, 0},  {1, 1}
    };
}

