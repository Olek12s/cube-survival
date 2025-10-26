package io.gith;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;


@Getter
public class Assets
{
    private final AssetManager manager = new AssetManager();
    private TextureAtlas tilesAtlas;
    private TextureAtlas entitiesAtlas;

    public Assets() {
        loadTileTextures();
        loadEntityTextures();
    }


    public TextureRegion getTileRegion(String name) {
        return tilesAtlas.findRegion(name);
    }
    public TextureRegion getEntityRegion(String name) {
        return entitiesAtlas.findRegion(name);
    }


    private void loadTileTextures() {
        manager.load("tiles.atlas", TextureAtlas.class);
        manager.finishLoading();
        tilesAtlas = manager.get("tiles.atlas", TextureAtlas.class);
    }

    private void loadEntityTextures() {
        manager.load("entities.atlas", TextureAtlas.class);
        manager.finishLoading();
        entitiesAtlas = manager.get("entities.atlas", TextureAtlas.class);
    }
}
