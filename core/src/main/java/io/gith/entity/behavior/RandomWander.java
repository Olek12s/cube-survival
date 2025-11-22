package io.gith.entity.behavior;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.gith.entity.entity.Entity;

public class RandomWander implements Behavior
{
    private Entity entity;

    private final Vector2 direction = new Vector2();
    private float moveTimer;
    private float idleTimer;
    private boolean moving;

    private static final float MOVE_TIME_MIN = 1.0f;
    private static final float MOVE_TIME_MAX = 3.0f;
    private static final float IDLE_TIME_MIN = 0.5f;
    private static final float IDLE_TIME_MAX = 2.0f;

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void start() {
        moving = false;
        idleTimer = MathUtils.random(IDLE_TIME_MIN, IDLE_TIME_MAX);
    }

    @Override
    public void tick(float dt) {
        if (entity == null) return;

        if (moving) {
            moveTimer -= dt;
            if (moveTimer <= 0) {
                stopMoving();
                idleTimer = MathUtils.random(IDLE_TIME_MIN, IDLE_TIME_MAX);
            } else {
                direction.nor();
                entity.setAiDesiredDirection(direction);
                entity.setWalking(true);
            }
        } else {
            idleTimer -= dt;
            if (idleTimer <= 0) {
                startMoving();
            } else {
                entity.getUpdater().getMovementVelocity().setZero();
            }
        }
    }

    @Override
    public void end() {
        entity.getUpdater().getMovementVelocity().setZero();
        moving = false;
    }

    private void startMoving() {
        float angle = MathUtils.random(0f, 360f);
        direction.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));
        moveTimer = MathUtils.random(MOVE_TIME_MIN, MOVE_TIME_MAX);
        moving = true;
        entity.setWalking(true);
    }

    private void stopMoving() {
        moving = false;
        entity.getUpdater().getMovementVelocity().setZero();
    }
}
