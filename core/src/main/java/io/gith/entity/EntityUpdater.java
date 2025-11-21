package io.gith.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.gith.CameraController;
import io.gith.Main;
import io.gith.Updatable;
import io.gith.tile.Tile;
import io.gith.utils.Direction;
import io.gith.utils.Hitbox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityUpdater implements Updatable
{
    private Entity entity;
    private final Vector2 movementVelocity = new Vector2();



    public EntityUpdater(Entity entity) {
        this.entity = entity;
    }


    @Override
    public void update(float dt) {
        checkHealthDespawnActionOnZeroOrLess();
        applyVelocity(dt);
        updateHitbox();
    }

    /**
     * Despawns entity if health is equal or below zero.
     */
    private void checkHealthDespawnActionOnZeroOrLess() {
        if (entity.currentHealth <= 0) {
            entity.despawn();
        }
    }
    private void applyVelocity(float dt) {
        Vector2 input = entity.getMovementInput(); // input direction, normalized or zero
        Vector2 targetVelocity;

        if (input.len2() > 0) { // player movement
            targetVelocity = input.cpy().scl(entity.getSpeed());
        } else {    // AI movement
            targetVelocity = entity.getAiDesiredDirection().cpy().scl(entity.getSpeed());
        }

        // acceleration // deacceleration
        if (targetVelocity.len2() > 0) {
            entity.getVelocity().lerp(targetVelocity, entity.getMovementAcceleration());
        } else {
            entity.getVelocity().lerp(new Vector2(0, 0), entity.getMovementDeacceleration());
        }

        if (entity.getVelocity().len2() > 0.01f)
        {
            entity.setWalking(true);
            entity.setDirection(Direction.fromVector(entity.getVelocity()));
        }
        else
        {
            entity.setWalking(false);
        }

        // Move entity with collisions
        Vector2 move = entity.getVelocity().cpy().scl(dt);
        float distance = move.len();
        float step = 1f;
        int steps = (int) Math.floor(distance / step);

        Vector2 stepDir = move.cpy().nor().scl(step);
        Vector2 newPos = entity.getWorldPosition().cpy();

        // check every axis separately for sliding
        for (int i = 0; i < steps; i++) {
            Vector2 testX = new Vector2(newPos.x + stepDir.x, newPos.y);
            if (!collidesAtPosition(testX)) newPos.x = testX.x;

            Vector2 testY = new Vector2(newPos.x, newPos.y + stepDir.y);
            if (!collidesAtPosition(testY)) newPos.y = testY.y;
        }

        // remaining movement less than step
        Vector2 remaining = move.cpy().sub(stepDir.cpy().scl(steps));
        Vector2 testX = new Vector2(newPos.x + remaining.x, newPos.y);
        if (!collidesAtPosition(testX)) newPos.x = testX.x;
        Vector2 testY = new Vector2(newPos.x, newPos.y + remaining.y);
        if (!collidesAtPosition(testY)) newPos.y = testY.y;

        entity.getWorldPosition().set(newPos);
    }

    private boolean collidesAtPosition(Vector2 position) {
        var tileMap = Main.getInstance().getTileMap();
        float tileSize = CameraController.TILE_SIZE;

        Hitbox testHitbox = entity.getHitbox();
        testHitbox.getPolygon().setPosition(position.x, position.y);

        // boundaries of tile positions
        Rectangle bounds = testHitbox.getBoundingBoxAABB();
        int startX = (int) Math.floor(bounds.x / tileSize);
        int startY = (int) Math.floor(bounds.y / tileSize);
        int endX = (int) Math.ceil((bounds.x + bounds.width) / tileSize);
        int endY = (int) Math.ceil((bounds.y + bounds.height) / tileSize);

        // checking tiles
        for (int tx = startX; tx < endX; tx++) {
            for (int ty = startY; ty < endY; ty++) {
                Tile tile = tileMap.getTileAtIndex(tx, ty);
                if (tile == null || !tile.isCollidable()) continue;

                if (testHitbox.overlapsTile(tile)) {
                    return true;
                }
            }
        }

        // checking other hitboxes
        //
        //
        return false;
    }

    private void updateHitbox() {
        entity.getHitbox().setPosition(entity.getWorldPosition().x, entity.getWorldPosition().y);
    }
}
