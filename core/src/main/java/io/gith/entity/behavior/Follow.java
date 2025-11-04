package io.gith.entity.behavior;

import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.entity.Entity;
import io.gith.tile.Tile;
import io.gith.tile.TileMapController;
import io.gith.utils.BFS;

import java.util.ArrayList;

public class Follow implements Behavior {

    private Entity entity;
    private Vector2 target;
    private ArrayList<Tile> path = new ArrayList<>();
    private int currentStep = 0;
    private float repathTimer = 0f;
    private final float repathInterval = 1.0f;
    private final boolean skipCollidables = true;

    public Follow(Vector2 target) {
        this.target = target;
    }

    @Override
    public void start() {
        recalcPath();
    }

    @Override
    public void tick(float dt) {
        if (entity == null || target == null) return;


        repathTimer += dt;
        if (repathTimer >= repathInterval) {
            recalcPath();
            repathTimer = 0f;
        }

        if (path == null || path.isEmpty() || currentStep >= path.size()) {
            entity.getEntityUpdater().getMovementVelocity().setZero();
            return;
        }

        Tile currentTile = path.get(currentStep);
        Vector2 tileWorldPos = currentTile.getWorldPosition();
        Vector2 pos = entity.getWorldPosition();

        Vector2 dir = new Vector2(tileWorldPos).sub(pos);


        if (dir.len() < 4f) {
            currentStep++;
            return;
        }

        dir.nor().scl(entity.getSpeed());
        entity.getEntityUpdater().getMovementVelocity().set(dir);
    }

    @Override
    public void end() {
        path.clear();
        currentStep = 0;
        if (entity != null)
            entity.getEntityUpdater().getMovementVelocity().setZero();
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    private void recalcPath() {
        TileMapController tileMap = Main.getInstance().getTileMap();

        Tile startTile = tileMap.getTileAtWorldPosition(
            entity.getWorldPosition().x,
            entity.getWorldPosition().y
        );

        Tile endTile = tileMap.getTileAtWorldPosition(
            target.x,
            target.y
        );

        if (startTile == null || endTile == null) return;

        path = BFS.findPathTiles(startTile, endTile, skipCollidables);
        currentStep = (path.size() > 1) ? 1 : 0;
    }
}
