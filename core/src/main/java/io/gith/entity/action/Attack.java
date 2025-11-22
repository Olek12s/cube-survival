package io.gith.entity.action;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.gith.entity.entity.Entity;
import io.gith.utils.Hitbox;

public class Attack implements Action
{
    private static final float MELEE_ATTACK_RANGE = 26;
    private static final float MELEE_ATTACK_WIDTH = 16;

    private Entity entitySource;
    private Entity entityTarget;

    private float timer = 0f;
    private float randomAttackWaitInterval = 0.5f;  // entity will attack player within [0s, 0.5s] when possible
    private boolean attackLaunched = false;

    public Attack(Entity entitySource, Entity entityTarget) {
        this.entitySource = entitySource;
        this.entityTarget = entityTarget;
    }

    @Override
    public void start()
    {
        // start the attack
        timer = 0f;
        attackLaunched = false;
        randomAttackWaitInterval = MathUtils.random(0f, 0.5f);   // random delay
    }

    @Override
    public void tick(float dt) {
        if (attackLaunched) return;

        timer += dt;
        if (timer >= randomAttackWaitInterval) {

            // Hitbox attack
            float[] attackVertices = new float[]{
                -MELEE_ATTACK_WIDTH / 2f, 0,
                MELEE_ATTACK_WIDTH / 2f, 0,
                MELEE_ATTACK_WIDTH / 2f, MELEE_ATTACK_RANGE,
                -MELEE_ATTACK_WIDTH / 2f, MELEE_ATTACK_RANGE
            };
            Hitbox attackHitbox = new Hitbox(attackVertices);

            // Vector between source and target
            Vector2 sourceCenter = entitySource.getHitbox().getMiddlePoint();
            Vector2 targetCenter = entityTarget.getHitbox().getMiddlePoint();
            Vector2 dir = targetCenter.cpy().sub(sourceCenter).nor();
            float angleDeg = dir.angleDeg();

            // rotate hitbox towards target
            attackHitbox.setPosition(sourceCenter.x, sourceCenter.y);
            attackHitbox.getPolygon().setRotation(angleDeg);

            // checking collision with target
            if (attackHitbox.overlaps(entityTarget.getHitbox())) {
                entityTarget.receiveDamage(entitySource.getBaseDamage(), false);
            }
            attackLaunched = true;
        }
    }

    @Override
    public void end() {
        attackLaunched = true;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entitySource = entity;
    }
}
