package io.gith;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class Assets
{
    private static final AssetManager manager = new AssetManager();
    private static final Map<Integer, TextureRegion> tileRegions = new HashMap<>();


    private static void loadTileTextures()
    {
        for (int i = 0; i < 15; i++) {
          //  manager.load("tiles/tile_" + i + ".png", Texture.class);
        }
        manager.finishLoading();
    }

    public static void createTileRegions() {
        for (int i = 0; i < 15; i++) {
            //Texture texture = manager.get("tiles/tile_" + i + ".png", Texture.class);
            //tileRegions.put(i, new TextureRegion(texture));
        }
    }

    public static TextureRegion getTileRegion(int id) {
        Texture texture = manager.get("tiles/tile_" + id + ".png", Texture.class);
        return new TextureRegion(texture);
    }

    public static void load()
    {
        loadTileTextures();
        createTileRegions();
    }
}
