package io.gith.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import io.gith.entity.Entity;
import io.gith.entity.behavior.Behavior;

public class Follow implements Behavior {

    private Entity entity;
    private Vector2 target;

    public Follow(Vector2 target) {
        this.target = target;
    }

    @Override
    public void start() {

    }

    @Override
    public void tick(float dt) {

    }

    @Override
    public void end() {

    }

    @Override
    public void setEntity(Entity entity) {

    }
}
