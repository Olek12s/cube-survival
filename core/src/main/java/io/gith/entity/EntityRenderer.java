package io.gith.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.gith.Main;
import io.gith.Renderable;

public class EntityRenderer implements Renderable
{
    private Entity entity;

    public EntityRenderer(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void renderTexture() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion region = Main.getInstance().getAssetsController().getEntityRegion(entity.id.getEntityName());

        if (region == null) System.out.println("No texture found for: " + entity.id.getEntityName());

        int textureSizeInAtlas = 16;
        int perRow = region.getRegionWidth() / textureSizeInAtlas;

        // TODO: animation
        int variant = 0;
        int x = (variant % perRow) * textureSizeInAtlas;
        int y = (variant / perRow) * textureSizeInAtlas;

        TextureRegion subReg = new TextureRegion(region, x, y, textureSizeInAtlas, textureSizeInAtlas);
        batch.draw(subReg, entity.worldPosition.x, entity.worldPosition.y);
    }

    @Override
    public void renderShape() {

    }
}
