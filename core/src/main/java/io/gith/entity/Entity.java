package io.gith.entity;

import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.Renderable;
import io.gith.Spawnable;
import io.gith.Updatable;

public abstract class Entity implements Spawnable
{
    private final Renderable entityRenderer;
    private final Updatable entityUpdater;
    private Vector2 worldPosition;
    private Vector2 velocity;
    private boolean spawned;

    public Entity(Vector2 startPosition) {
        entityRenderer = new EntityRenderer();
        entityUpdater = new EntityUpdater();
        Main.getInstance().getRenderables().add(entityRenderer);
        Main.getInstance().getUpdatables().add(entityUpdater);

        this.worldPosition = new Vector2(startPosition);
        this.velocity = new Vector2(0, 0);
    }

    public void spawn() {
        if (spawned) return;
        Main.getInstance().getRenderables().add(entityRenderer);
        Main.getInstance().getUpdatables().add(entityUpdater);
    }

    public void despawn() {
        if (!spawned) return;
        Main.getInstance().getRenderables().remove(entityRenderer);
        Main.getInstance().getUpdatables().remove(entityUpdater);
    }
}
