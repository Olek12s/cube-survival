package io.gith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
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

    private static float CAMERA_SPEED = 8000f;

    private float zoom = 1f;
    private static final float ZOOM_SPEED = 0.1f;
    private static final float MIN_ZOOM = 0.3f;
    private static final float MAX_ZOOM = 150f;

    public CameraController() {
        Main.getInstance().getUpdatables().add(this);
        camera = new OrthographicCamera();
        viewport = new FitViewport(BASE_TILES_X * TILE_SIZE, BASE_TILES_Y * TILE_SIZE, camera);

        camera.position.set(0, 0, 0);
        camera.update();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                zoom += amountY * ZOOM_SPEED;
                zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom));
                camera.zoom = zoom;
                return true;
            }
        });
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        //camera.position.set(0, 0, 0);
        camera.update();
    }

    public boolean isPositionVisible(float worldX, float worldY) {
        float halfWidth = (camera.viewportWidth * camera.zoom) / 2f;
        float halfHeight = (camera.viewportHeight * camera.zoom) / 2f;

        float worldLeft = camera.position.x - halfWidth;
        float worldRight = camera.position.x + halfWidth;
        float worldBottom = camera.position.y - halfHeight;
        float worldTop = camera.position.y + halfHeight;

        return worldX >= worldLeft && worldX <= worldRight &&
            worldY >= worldBottom && worldY <= worldTop;
    }

    public boolean isRectVisible(float x, float y, float width, float height) {
        float camLeft = camera.position.x - (viewport.getWorldWidth() * camera.zoom) / 2f;
        float camRight = camera.position.x + (viewport.getWorldWidth() * camera.zoom) / 2f;
        float camBottom = camera.position.y - (viewport.getWorldHeight() * camera.zoom) / 2f;
        float camTop = camera.position.y + (viewport.getWorldHeight() * camera.zoom) / 2f;

        float chunkLeft = x;
        float chunkRight = x + width;
        float chunkBottom = y;
        float chunkTop = y + height;

        return !(chunkRight < camLeft || chunkLeft > camRight || chunkTop < camBottom || chunkBottom > camTop);
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
        camera.update();
    }
}
