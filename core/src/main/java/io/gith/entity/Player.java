package io.gith.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.gith.utils.Direction;

public class Player extends Entity
{

    public Player(Vector2 startPosition) {
        super(EntityID.PLAYER, startPosition);
        speed = 80f;
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        super.update(dt);
    }

    private void handleInput(float dt) {
        Vector2 inputDir = new Vector2();
        isWalking = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            inputDir.y += 1;
            isWalking = true;
            direction = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            inputDir.y -= 1;
            isWalking = true;
            direction = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            inputDir.x -= 1;
            isWalking = true;
            direction = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            inputDir.x += 1;
            isWalking = true;
            direction = Direction.RIGHT;
        }
        if (velocity.len2() > 0) velocity.nor().scl(speed);

        if (inputDir.len2() > 0) {
            inputDir.nor(); // direction
            entityUpdater.getMovementVelocity().set(inputDir).scl(speed); // velocity = speed * direction
        }
        else {
            entityUpdater.getMovementVelocity().set(0, 0);
            isWalking = false;
        }
    }
}
