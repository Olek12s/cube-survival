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
    public static int MAX_UPS = 100000;   // logic updates per second
    public static int MAX_FPS = 100000;   // rendering frames per second
    private float logicInterval = 1f / MAX_UPS;  // seconds per logic update
    private float accumulator = 0; // acc λt
    private long lastRenderTime = 0; // to limit FPS
    public static float currentFPS = 0;
    public static float currentUPS = 0;
    private long lastFrameTime = System.nanoTime();
    private long lastUpdateTime = System.nanoTime();

    public static float lastUpdateTimeUs = 0;
    public static float lastRenderTimeUs = 0;
    public static float lastFrameTimeUs = 0;
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

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;

       // gui.startFrame(); // start ImGui frame

        int maxUpdatesPerFrame = MAX_UPS / MAX_FPS;
        int updatesThisFrame = 0;

        long updateStart = System.nanoTime();
        while (accumulator >= logicInterval && updatesThisFrame < maxUpdatesPerFrame) {
            for (Updatable u : updatables) {
                u.update(logicInterval);
            }
            accumulator -= logicInterval;
            updatesThisFrame++;
        }
        long updateEnd = System.nanoTime();
        lastUpdateTimeUs = (updateEnd - updateStart) / 1_000f;

        if (updatesThisFrame == maxUpdatesPerFrame) {
            accumulator = 0;
        }

        if (MAX_FPS > 0) {
            long now = System.nanoTime();
            long minFrameTime = 1_000_000_000L / MAX_FPS;
            if (lastRenderTime > 0) {
                long frameDuration = now - lastRenderTime;
                if (frameDuration < minFrameTime) {
                    try {
                        Thread.sleep((minFrameTime - frameDuration) / 1_000_000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            lastRenderTime = System.nanoTime();
        }
        long now = System.nanoTime();
        currentUPS = updatesThisFrame / ((now - lastUpdateTime) / 1_000_000_000f);
        lastUpdateTime = now;

        long renderStart = System.nanoTime();
        draw();
        long renderEnd = System.nanoTime();
        lastRenderTimeUs = (renderEnd - renderStart) / 1_000f;
        currentFPS = 1f / ((now - lastFrameTime) / 1_000_000_000f);
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
