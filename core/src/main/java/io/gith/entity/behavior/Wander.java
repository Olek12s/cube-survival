package io.gith.entity.behavior;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.gith.entity.Entity;
import io.gith.utils.Direction;

public class Wander implements Behavior
{
    private Entity entity;

    private float moveTimer;
    private float idleTimer;
    private Vector2 direction;
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
        direction = new Vector2();
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
                entity.getVelocity().set(direction).scl(entity.getSpeed());
                entity.setDirection(Direction.fromVector(entity.getVelocity()));
            }
        } else {
            idleTimer -= dt;
            if (idleTimer <= 0) {
                startMoving();
                entity.setWalking(true);
            } else {
                entity.getVelocity().setZero();
                entity.setWalking(false);
            }
        }
    }

    @Override
    public void end() {
        entity.getVelocity().setZero();
        entity.setWalking(false);
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
        entity.setWalking(false);
        entity.getVelocity().setZero();
    }
}
