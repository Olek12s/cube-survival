package io.gith.entity;

import com.badlogic.gdx.math.Vector2;
import io.gith.Renderable;
import io.gith.Spawnable;
import io.gith.Updatable;
import io.gith.entity.behavior.Behavior;
import io.gith.utils.Direction;
import io.gith.utils.Hitbox;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Entity implements Spawnable
{
    protected final ArrayList<Behavior> behaviors;
    protected final EntityRenderer entityRenderer;
    protected final EntityUpdater entityUpdater;
    protected boolean spawned;
    protected Vector2 worldPosition;
    protected Direction direction;
    protected Vector2 velocity;
    protected Hitbox hitbox;
    protected float speed = 400f;
    protected boolean isWalking;
    protected EntityID id;

    public Entity(EntityID id, Vector2 worldPosition) {
        this.id = id;
        entityRenderer = new EntityRenderer(this);
        entityUpdater = new EntityUpdater(this);
        this.worldPosition = worldPosition;
        this.velocity = new Vector2(0, 0);
        this.behaviors = new ArrayList<>();
        float[] vertices = new float[]{
            0,0,
            16,0,
            16,16,
            0,16,
        };

        hitbox = new Hitbox(vertices);
        this.direction = Direction.DOWN;
        spawn();
    }

    @Override
    public void spawn() {

        Spawnable.super.spawn();
    }

    @Override
    public void despawn() {
        Spawnable.super.despawn();
    }

    @Override
    public void update(float dt) {
        entityUpdater.update(dt);

        for (Behavior b : behaviors) {
            b.tick(dt);
        }
    }

    @Override
    public void renderTexture() {
        entityRenderer.renderTexture();
    }

    @Override
    public void renderShape() {
        entityRenderer.renderShape();
    }

    @Override
    public EntityRenderer getRenderer() {return entityRenderer;}

    @Override
    public EntityUpdater getUpdater() {return entityUpdater;}

    @Override
    public boolean isSpawned() {return spawned;}

    @Override
    public void setSpawned(boolean value) {spawned = value;}
}
