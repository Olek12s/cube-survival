package io.gith.entity.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.gith.CameraController;
import io.gith.Main;
import io.gith.Updatable;
import io.gith.tile.Chunk;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;
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
        updateChunkAssocation();

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

        TileMapController tileMap = Main.getInstance().getTileMap();
        Chunk targetChunk = tileMap.getChunkFromWorldPosition(newPos);

        // check every axis separately for sliding
        for (int i = 0; i < steps; i++) {
            // step X
            Vector2 testX = new Vector2(newPos.x + stepDir.x, newPos.y);
            Chunk chunkX = tileMap.getChunkFromWorldPosition(testX);
            if (!collidesAtPosition(testX) && chunkX != null && chunkX.isWithinChunk(testX)) {
                newPos.x = testX.x;
            }

            // step Y
            Vector2 testY = new Vector2(newPos.x, newPos.y + stepDir.y);
            Chunk chunkY = tileMap.getChunkFromWorldPosition(testY);
            if (!collidesAtPosition(testY) && chunkY != null && chunkY.isWithinChunk(testY)) {
                newPos.y = testY.y;
            }
        }


        // remaining movement less than step
        Vector2 remaining = move.cpy().sub(stepDir.cpy().scl(steps));

        // step X
        Vector2 testX = new Vector2(newPos.x + remaining.x, newPos.y);
        Chunk chunkX = tileMap.getChunkFromWorldPosition(testX);
        if (!collidesAtPosition(testX) && chunkX != null && chunkX.isWithinChunk(testX)) {
            newPos.x = testX.x;
        }

        // step Y
        Vector2 testY = new Vector2(newPos.x, newPos.y + remaining.y);
        Chunk chunkY = tileMap.getChunkFromWorldPosition(testY);
        if (!collidesAtPosition(testY) && chunkY != null && chunkY.isWithinChunk(testY)) {
            newPos.y = testY.y;
        }
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

        // checking other hitboxes within current chunk + 8 neighboring
        Chunk currentChunk = entity.getCurrentChunk();

        if (currentChunk != null) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int neighborChunkX = (int) currentChunk.getChunkCoords().x + dx;
                    int neighborChunkY = (int) currentChunk.getChunkCoords().y + dy;
                    Chunk neighborChunk = tileMap.getChunkFromMap(neighborChunkX, neighborChunkY);
                    if (neighborChunk == null) continue;

                    for (Entity other : neighborChunk.getEntities()) {
                        if (other == entity) continue; // don't check self
                        if (testHitbox.overlaps(other.getHitbox())) {
                            return true;
                        }
                    }
                }
            }
        }


        return false;
    }
    private void updateHitbox() {
        entity.getHitbox().setPosition(entity.getWorldPosition().x, entity.getWorldPosition().y);
    }
    private void updateChunkAssocation() {
        Chunk currentChunk = Main.getInstance().getTileMap().getChunkFromWorldPosition(entity.getWorldPosition());

        // if entity had no associated chunk before
        if (entity.getCurrentChunk() == null) {
            if (currentChunk != null) {
                entity.setCurrentChunk(currentChunk);
                // System.out.println("Added not associated entity to the: " + currentChunk);
                currentChunk.getEntities().add(entity);
            }
            return;
        }

        // if entity left its associated chunk
        if (!entity.getCurrentChunk().isWithinChunk(entity.getWorldPosition())) {
            entity.getCurrentChunk().getEntities().remove(entity);  // remove from the old chunk

            // add entity to the new chunk
            if (currentChunk != null) {
                entity.setCurrentChunk(currentChunk);
                // System.out.println("Entity changed chunk to the: " + currentChunk);
                currentChunk.getEntities().add(entity);
            }
            else {
                // System.out.println("Entity changed chunk to null");
                entity.setCurrentChunk(null);   // if chunk was not loaded - set chunk to the null
            }
        }
    }
}
