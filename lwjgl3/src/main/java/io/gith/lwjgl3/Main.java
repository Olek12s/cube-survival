package io.gith.lwjgl3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
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
    public static int MAX_UPS = 100;   // logic updates per second
    public static int MAX_FPS = 100;   // rendering frames per second
    private float logicInterval = 1f / MAX_UPS;  // seconds per logic update
    private float accumulator = 0; // acc λt
    private long lastRenderTime = 0; // to limit FPS
    public static float currentFPS = 0;
    public static float currentUPS = 0;
    private long lastFrameTime = System.nanoTime();
    private long lastUpdateTime = System.nanoTime();
    ///////////////////     main loop       ///////////////////

    ///////////////////     controllers       ///////////////////
    private CameraController cameraController;
    private Gui gui;
    ///////////////////     controllers       ///////////////////


    public void create()
    {
        if (instance == null) instance = this;

        updatables = new ArrayList<>();
        renderables = new ArrayList<>();
        batch = new SpriteBatch();

        cameraController = new CameraController();
        gui = new Gui();

    }

    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;

        int maxUpdatesPerFrame = MAX_UPS / MAX_FPS;
        int updatesThisFrame = 0;

        while (accumulator >= logicInterval && updatesThisFrame < maxUpdatesPerFrame) {
            for (Updatable u : updatables) {
                u.update(logicInterval);
            }
            accumulator -= logicInterval;
            updatesThisFrame++;
        }

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

        draw();

        long frameNow = System.nanoTime();
        currentFPS = 1f / ((frameNow - lastFrameTime) / 1_000_000_000f);
        lastFrameTime = frameNow;
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
