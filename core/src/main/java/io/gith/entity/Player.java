package io.gith.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.Renderable;
import io.gith.Updatable;
import io.gith.utils.Direction;

public class Player extends Entity
{

    public Player(Vector2 startPosition) {
        super(EntityID.PLAYER, startPosition);
    }


    @Override
    public void update(float dt) {
        super.update(dt);
        handleInput(dt);

        if (Main.getInstance().getTileMap().getTileAtWorldPosition(worldPosition.x, worldPosition.y) == null) return;

        if (Main.getInstance().getTileMap().getTileAtWorldPosition(worldPosition.x, worldPosition.y).isCollidable()) {
           System.out.println("collidable");
        }
        else {
            System.out.println("non collidable");
        }
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
