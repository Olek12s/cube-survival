package io.gith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class InputController implements Updatable
{

    public InputController() {
        Main.getInstance().getUpdatables().add(this);
    }

    public void update(float dt) {
        CameraController camera = Main.getInstance().getCameraController();
        float CAMERA_SPEED = 200f * CameraController.TILE_SIZE;
        Vector2 movement = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.y += CAMERA_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.y -= CAMERA_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.x -= CAMERA_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.x += CAMERA_SPEED * dt;
        }

        camera.getCamera().translate(movement);
    }
}
