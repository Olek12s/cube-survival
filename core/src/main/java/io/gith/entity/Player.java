package io.gith.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.gith.Renderable;
import io.gith.Updatable;

public class Player extends Entity
{

    public Player(Vector2 startPosition) {
        super(EntityID.PLAYER, startPosition);
    }


    @Override
    public void update(float dt) {
        super.update(dt);

        handleInput(dt);
        applyVelocity(dt);
    }

    private void handleInput(float dt) {
        Vector2 inputDir = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) inputDir.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) inputDir.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) inputDir.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) inputDir.x += 1;
        if (velocity.len2() > 0) velocity.nor().scl(speed);

        if (inputDir.len2() > 0) {
            inputDir.nor(); // direction
            velocity.set(inputDir.scl(speed)); // velocity = speed * direction
        } else {
            velocity.setZero();
        }
    }

    /*
    Move entity by velocity vector
     */
    private void applyVelocity(float dt) {
        worldPosition.mulAdd(velocity, dt);
    }
}
