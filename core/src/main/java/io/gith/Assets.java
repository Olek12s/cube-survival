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

    public Assets() {
        loadTileTextures();
    }


    public TextureRegion getTileRegion(String name) {
        return tilesAtlas.findRegion(name);
    }


    private void loadTileTextures() {
        manager.load("tiles.atlas", TextureAtlas.class);
        manager.finishLoading();
        tilesAtlas = manager.get("tiles.atlas", TextureAtlas.class);
    }
}
