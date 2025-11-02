package io.gith.entity;

import com.badlogic.gdx.math.Vector2;
import io.gith.entity.behavior.Behavior;
import io.gith.entity.behavior.Wander;

public class Slime extends Entity
{
    private final Behavior wander;

    public Slime(Vector2 worldPosition) {
        super(EntityID.SLIME, worldPosition);
        this.wander = new Wander();
        this.wander.setEntity(this);
        this.wander.start();
        behaviors.add(wander);
        speed = 36f;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
