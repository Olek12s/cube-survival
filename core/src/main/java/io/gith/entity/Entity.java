package io.gith.entity;

import com.badlogic.gdx.math.Vector2;
import io.gith.Renderable;
import io.gith.Spawnable;
import io.gith.Updatable;
import io.gith.entity.behavior.Behavior;
import io.gith.tile.Chunk;
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
    private Chunk currentChunk;
    protected final EntityRenderer entityRenderer;
    protected final EntityUpdater entityUpdater;
    protected boolean spawned;
    protected Vector2 worldPosition;
    protected Direction direction;
    protected Vector2 velocity;
    protected Hitbox hitbox;
    private Vector2 aiDesiredDirection = new Vector2();
    protected float speed = 800f;
    protected float movementAcceleration = 0.2f;    // % max speed per tick
    protected float movementDeacceleration = 0.33f;  // % max speed per tick
    protected boolean isWalking;
    protected EntityID id;
    protected float currentHealth;
    protected float currentEnergy;

    // TODO: turn into JSON values
    private float maxHealth = 20;
    private float maxEnergy = 20;

    public Entity(EntityID id, Vector2 worldPosition) {
        this.id = id;
        entityRenderer = new EntityRenderer(this);
        entityUpdater = new EntityUpdater(this);
        this.worldPosition = worldPosition;
        this.velocity = new Vector2(0, 0);
        this.behaviors = new ArrayList<>();
        this.currentHealth = maxHealth;
        this.currentEnergy = maxEnergy;

        int frameSize = 16;
        int hitboxSize = 14;
        float offsetX = (frameSize - hitboxSize) / 2f;
        float offsetY = (frameSize - hitboxSize) / 2f;
        float[] vertices = new float[] {
            offsetX, offsetY,
            offsetX + hitboxSize, offsetY,
            offsetX + hitboxSize, offsetY + hitboxSize,
            offsetX, offsetY + hitboxSize
        };
        hitbox = new Hitbox(vertices);


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

    public Vector2 getMovementInput() {
        return Vector2.Zero; // default: no input
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
