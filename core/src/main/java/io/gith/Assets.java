package io.gith;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class Assets
{
    private final AssetManager manager = new AssetManager();
    private TextureAtlas tilesAtlas;

    public Assets() {
        loadTileTextures();
    }


    public TextureRegion getTileRegion(int id) {
        return tilesAtlas.findRegion("tile_" + id);
    }

    public TextureRegion getTileRegion(String name) {
        return tilesAtlas.findRegion(name);
    }


    private static void loadTileTextures()
    {
    }
}
