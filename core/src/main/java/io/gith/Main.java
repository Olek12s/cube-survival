package io.gith;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import io.gith.tile.Chunk;
import io.gith.tile.Tile;
import io.gith.tile.TileID;
import io.gith.tile.TileMapController;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Main extends Game
{
    @Getter
    private static Main instance;

    public SpriteBatch batch;
    private ArrayList<Updatable> updatables;
    private ArrayList<Renderable> renderables;

    ///////////////////     main loop       ///////////////////
    public static int MAX_UPS = 75;   // logic updates per second
    public static int MAX_FPS = 75;   // rendering frames per second
    private float logicInterval = 1f / MAX_UPS;  // seconds per logic update
    private float accumulator = 0; // acc λt

    ///////////////////     performance metrics       ///////////////////
    public static float currentFPS = 0f;
    public static float currentUPS = 0f;
    public static float lastUpdateTimeUs = 0;
    public static float lastRenderTimeUs = 0;
    public static float lastFrameTimeUs = 0;

    private long lastUpdateTime = System.nanoTime();
    private long lastRenderTime = System.nanoTime();
    private long lastFrameTime = System.nanoTime();
    ///////////////////     main loop       ///////////////////

    ///////////////////     controllers       ///////////////////
    private InputController inputController;
    private CameraController cameraController;
    private Assets assetsController;
    private TileMapController tileMap;
    ///////////////////     controllers       ///////////////////

    public void create()
    {
        if (instance == null) instance = this;

        updatables = new ArrayList<>();
        renderables = new ArrayList<>();
        batch = new SpriteBatch();
        assetsController = new Assets();
        inputController = new InputController();
        cameraController = new CameraController();
        tileMap = new TileMapController();
        tileMap.loadFirstChunks();
    }


    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        accumulator += dt;

        long updateStart = System.nanoTime();
        int updatesThisFrame = 0;

        // --- update ---
        while (accumulator >= logicInterval) {
            for (Updatable u : updatables) {
                u.update(logicInterval);
            }
            accumulator -= logicInterval;
            updatesThisFrame++;
        }

        long updateEnd = System.nanoTime();
        lastUpdateTimeUs = (updateEnd - updateStart) / 1_000f; // µs

        // --- UPS ---
        long now = System.nanoTime();
        float elapsedSecSinceLastUpdate = (now - lastUpdateTime) / 1_000_000_000f;
        if (elapsedSecSinceLastUpdate > 0) {
            currentUPS = updatesThisFrame / elapsedSecSinceLastUpdate;
        }
        lastUpdateTime = now;


        // --- render ---
        long renderStart = System.nanoTime();
        draw();
        long renderEnd = System.nanoTime();
        lastRenderTimeUs = (renderEnd - renderStart) / 1_000f; // µs

        // --- FPS ---
        float frameTimeSec = (now - lastFrameTime) / 1_000_000_000f;
        if (frameTimeSec > 0) {
            currentFPS = 1f / frameTimeSec;
        }
        lastFrameTime = now;

        lastFrameTimeUs = lastUpdateTimeUs + lastRenderTimeUs;
    }




    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        cameraController.getCamera().update();

        batch.setProjectionMatrix(cameraController.getCamera().combined);
        batch.begin();
        for (Renderable r : renderables) {
            r.render();
        }
        batch.end();
    }

    public void resize (int width, int height) {
        cameraController.resize(width, height);
    }

    public void pause () {
    }

    public void resume () {
    }

    public void dispose()
    {
        batch.dispose();
    }
}
