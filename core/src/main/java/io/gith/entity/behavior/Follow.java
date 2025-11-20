package io.gith.entity.behavior;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.gith.CameraController;
import io.gith.Main;
import io.gith.entity.Entity;
import io.gith.tile.Tile;
import io.gith.utils.Astar;
import io.gith.utils.BFS;
import io.gith.utils.Pathfinder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Random;

@Getter
public class Follow implements Behavior {
    private Entity entity;
    private final Vector2 positionToFollow;
    private ArrayList<Tile> tilePath;
    private Rectangle hitboxAABB;
    private Pathfinder pathfinder;

    private final float RECALCULATE_INTERVAL;    // x seconds
    private float recalcTimer = 0f;
    private final int PRECISION_MARGIN = 12;            // world units error tolerance - how many units entity's hitbox middle point can differ from tile's middle point

    public Follow(Vector2 positionToFollow) {
        Random random = new Random();
        this.RECALCULATE_INTERVAL = 0.20f + random.nextFloat() * (0.34f - 0.20f);   // [0.20, 0.33]
        this.positionToFollow = positionToFollow;
        this.tilePath = new ArrayList<>();
        this.pathfinder = new Astar();
    }

    @Override
    public void start() {
        recalculatePath();
    }

    private void recalculatePath() {
        if (entity == null) return;

        Tile startTile = Main.getInstance().getTileMap().getTileAtWorldPosition(entity.getWorldPosition());
        Tile endTile = Main.getInstance().getTileMap().getTileAtWorldPosition(positionToFollow);

        if (startTile != null && endTile != null) {
            this.tilePath = pathfinder.findPathTiles(startTile, endTile, true, false);

            // delete first tile to prevent "shaking" for 1 tick after recalculating path
            if (tilePath.size() > 2)
            {
                tilePath.removeFirst();
            }
        }
    }

    @Override
    public void tick(float dt) {
        if (entity == null) return;

        // update hitbox to the current update tick
        hitboxAABB = entity.getHitbox().getBoundingBoxAABB();

        // recalculate path if timer reached interval
        recalcTimer += dt;
        if (recalcTimer >= RECALCULATE_INTERVAL) {
            recalcTimer = 0f;
            recalculatePath();
        }

        // if no path - stay in place and stop walking
        if (tilePath == null || tilePath.isEmpty()) {
            entity.getUpdater().getMovementVelocity().set(0, 0);
            entity.setWalking(false);
            return;
        }

        // set target to follow as next tile from the list
        Tile targetTile = tilePath.getFirst();
        Vector2 targetCenter = new Vector2(
            targetTile.getWorldX() + CameraController.TILE_SIZE / 2f,
            targetTile.getWorldY() + CameraController.TILE_SIZE / 2f
        );

        Vector2 entityCenter = entity.getHitbox().getMiddlePoint();
        Vector2 direction = targetCenter.cpy().sub(entityCenter);

        // if entity is close enough to the middle point of the target tile
        if (direction.len() < PRECISION_MARGIN) {
            tilePath.removeFirst(); // remove target tile

            // if no path - stay in place and stop walking
            if (tilePath.isEmpty()) {
                entity.getUpdater().getMovementVelocity().set(0, 0);
                entity.setWalking(false);
                return;
            }
            else    // else - set target to follow as next tile from the list
            {
                targetTile = tilePath.getFirst();
                targetCenter.set(
                    targetTile.getWorldX() + CameraController.TILE_SIZE / 2f,
                    targetTile.getWorldY() + CameraController.TILE_SIZE / 2f
                );
                direction.set(targetCenter).sub(entityCenter);
            }
        }
        // go to the target tile
        direction.nor();
        entity.getUpdater().getMovementVelocity().set(direction).scl(entity.getSpeed());
        entity.setWalking(true);
    }

    @Override
    public void end() {
        tilePath.clear();
        entity.getUpdater().getMovementVelocity().set(0, 0);
        entity.setWalking(false);
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.hitboxAABB = entity.getHitbox().getBoundingBoxAABB();
    }

    public ArrayList<Tile> getPath() {
        return tilePath;
    }
}
