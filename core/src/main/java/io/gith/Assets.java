package io.gith;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Assets
{
    private final AssetManager manager = new AssetManager();
    private TextureAtlas tilesAtlas;


    private static Map<Integer, String> tileMap;

    public static Map<Integer, String> getTileMap() {
        return tileMap;
    }

    public Assets() {
        loadTileTextures();
        setupTileMap();
    }


    public TextureRegion getTileRegion(String name) {
        return tilesAtlas.findRegion(name);
    }

    public TextureRegion getTileRegion(int id) {
        return tilesAtlas.getRegions().get(id);
    }

    private void setupTileMap() {
        tileMap = Map.of(
            0, "grass",
            1, "stone",
            2, "sand"
        );
    }


    private void loadTileTextures() {
        manager.load("tiles.atlas", TextureAtlas.class);
        manager.finishLoading();
        tilesAtlas = manager.get("tiles.atlas", TextureAtlas.class);
    }
}
