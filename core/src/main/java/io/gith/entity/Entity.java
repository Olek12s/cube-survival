package io.gith.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.Renderable;
import io.gith.Spawnable;
import io.gith.Updatable;
import lombok.Getter;

@Getter
public abstract class Entity implements Spawnable
{
    protected final Renderable entityRenderer;
    protected final Updatable entityUpdater;
    protected boolean spawned;
    protected Vector2 worldPosition;
    protected Vector2 velocity;
    protected float speed = 24f;
    protected EntityID id;

    public Entity(EntityID id, Vector2 worldPosition) {
        entityRenderer = new EntityRenderer(this);
        entityUpdater = new EntityUpdater(this);
        this.id = id;
        this.worldPosition = worldPosition;
        this.velocity = new Vector2(0, 0);


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
    public Renderable getRenderer() {return entityRenderer;}

    @Override
    public Updatable getUpdater() {return entityUpdater;}

    @Override
    public boolean isSpawned() {return spawned;}

    @Override
    public void setSpawned(boolean value) {spawned = value;}
}
