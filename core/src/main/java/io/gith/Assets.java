package io.gith;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import java.awt.*;


@Getter
public class Assets
{
    private final AssetManager manager = new AssetManager();
    private TextureAtlas tilesAtlas;
    private TextureAtlas entitiesAtlas;
    private TextureAtlas GUIAtlas;
    private BitmapFont font;

    public Assets() {
        loadTileTextures();
        loadEntityTextures();
        loadGUITextures();
        loadFont();
    }


    public TextureRegion getTileRegion(String name) {
        return tilesAtlas.findRegion(name);
    }
    public TextureRegion getEntityRegion(String name) {
        return entitiesAtlas.findRegion(name);
    }
    public TextureRegion getGUIRegion(String name) { return GUIAtlas.findRegion(name); }
    public BitmapFont getFont() {return font;}

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

    private void loadGUITextures() {
        manager.load("gui.atlas", TextureAtlas.class);
        manager.finishLoading();
        GUIAtlas = manager.get("gui.atlas", TextureAtlas.class);
    }



    private void loadFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        //parameter.size = 24;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }
}
