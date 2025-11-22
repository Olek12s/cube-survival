package io.gith.entity.entity;

import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.entity.behavior.Behavior;
import io.gith.entity.behavior.Follow;
import io.gith.entity.behavior.RandomWander;

public class Slime extends Entity
{
    private final Behavior wander;
    private final Behavior follow;

    public Slime(Vector2 worldPosition) {
        super(EntityID.SLIME, worldPosition);


        this.wander = new RandomWander();
        this.wander.setEntity(this);
        this.wander.start();
        behaviors.add(wander);

        speed = 180f;

        follow = new Follow(Main.getInstance().getPlayer().getWorldPosition());
        follow.setEntity(this);
        follow.start();
        behaviors.add(follow);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
