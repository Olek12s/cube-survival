package io.gith;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;

@Getter
public class CameraController
{
    private Viewport viewport;
    private OrthographicCamera camera;
    public static final float TILE_SIZE = 64;

    private static final int BASE_TILES_X = 16;
    private static final int BASE_TILES_Y = 9;

    public CameraController() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(BASE_TILES_X * TILE_SIZE, BASE_TILES_Y * TILE_SIZE, camera);

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }
}
