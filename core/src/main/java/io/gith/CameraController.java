package io.gith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;

@Getter
public class CameraController implements Updatable
{
    private Viewport viewport;
    private OrthographicCamera camera;
    public static final float TILE_SIZE = 64;
    private static final int BASE_TILES_X = 16;
    private static final int BASE_TILES_Y = 9;

    private static float CAMERA_SPEED = 200f;

    public CameraController() {
        Main.getInstance().getUpdatables().add(this);
        camera = new OrthographicCamera();
        viewport = new FitViewport(BASE_TILES_X * TILE_SIZE, BASE_TILES_Y * TILE_SIZE, camera);

        camera.position.set(0, 0, 0);
        camera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(0, 0, 0);
        camera.update();
    }


    @Override
    public void update(float dt) {
        Vector2 movement = new Vector2();

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
        camera.translate(movement);
    }
}
