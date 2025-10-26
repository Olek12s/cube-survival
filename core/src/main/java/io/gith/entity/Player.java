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
        System.out.println("player");
        handleInput(dt);
    }

    private void handleInput(float dt) {
        velocity.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) velocity.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) velocity.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) velocity.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) velocity.x += 1;
        if (velocity.len2() > 0) velocity.nor().scl(speed);
    }
}
