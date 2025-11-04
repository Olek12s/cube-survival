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
        applyVelocity(dt);
        updateHitbox();
    }

    private void applyVelocity(float dt) {
        Vector2 finalVelocity = new Vector2().add(movementVelocity);    // sum all vectors influencing entity (walking, knockback, push...)
        entity.getVelocity().set(finalVelocity);

        if (finalVelocity.len2() > 0) {
            entity.setWalking(true);
            entity.setDirection(Direction.fromVector(finalVelocity));
        }
        else {
            entity.setWalking(false);
            return;
        }

        Vector2 move = entity.velocity.cpy().scl(dt);
        float distance = move.len();
        float step = 1f;
        int steps = (int) Math.floor(distance / step);

        Vector2 stepDir = move.cpy().nor().scl(step);
        Vector2 newPos = entity.worldPosition.cpy();

        // check every axis separately for "sliding" effect
        for (int i = 0; i < steps; i++) {
            // X axis steps
            Vector2 testX = new Vector2(newPos.x + stepDir.x, newPos.y);
            if (!collidesAtPosition(testX)) {
                newPos.x = testX.x;
            }

            // Y axis steps
            Vector2 testY = new Vector2(newPos.x, newPos.y + stepDir.y);
            if (!collidesAtPosition(testY)) {
                newPos.y = testY.y;
            }
        }

        // rest of the move, where distance < step
        // remaining = move - stepDir * steps
        Vector2 remaining = move.cpy().sub(stepDir.cpy().scl(steps));

        // check every axis separately for "sliding" effect
        Vector2 testX = new Vector2(newPos.x + remaining.x, newPos.y);
        if (!collidesAtPosition(testX)) {
            newPos.x = testX.x;
        }
        Vector2 testY = new Vector2(newPos.x, newPos.y + remaining.y);
        if (!collidesAtPosition(testY)) {
            newPos.y = testY.y;
        }

        entity.worldPosition.set(newPos);   // update entity's position
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
