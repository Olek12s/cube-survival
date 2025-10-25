package io.gith.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.gith.*;
import lombok.Getter;
import lombok.Setter;

import static io.gith.CameraController.TILE_SIZE;

@RenderingOrder(Order.TILE)
public class Tile  {
    private Vector2 indexPosition;
    @Getter @Setter
    private TileID id;
    private boolean collidable;
    @Setter
    private transient byte bitmask;

    public Vector2 getIndexPosition() {return indexPosition;}
    public Vector2 getWorldPosition() {return new Vector2(getWorldX(), getWorldY());}
    public int getWorldX() {return (int)(indexPosition.x * TILE_SIZE);}
    public int getWorldY() {return (int)(indexPosition.y * TILE_SIZE);}


    private Tile(Builder builder) {
        this.indexPosition = builder.position;
        this.id = builder.id;
        this.collidable = builder.collidable;
    }

    public void updateBitmask() {
        Autotiler.assignBitmask(this);
    }

    protected void render() {
        SpriteBatch batch = Main.getInstance().getBatch();
        TextureRegion region = Main.getInstance().getAssetsController().getTileRegion(id.getTileName());

        if (region == null) System.out.println("No texture found for: " + id.getTileName());

        int tileSizeInAtlas = 24;
        int tilesPerRow = region.getRegionWidth() / tileSizeInAtlas;

        int variant = Autotiler.getTextureVariantFromMask(bitmask);
        int x = (variant % tilesPerRow) * tileSizeInAtlas;
        int y = (variant / tilesPerRow) * tileSizeInAtlas;

        TextureRegion subTile = new TextureRegion(region, x, y, tileSizeInAtlas, tileSizeInAtlas);
        batch.draw(subTile, indexPosition.x * TILE_SIZE, indexPosition.y * TILE_SIZE);
    }

    protected void renderOutline() {
        float x = indexPosition.x * TILE_SIZE;
        float y = indexPosition.y * TILE_SIZE;
        ShapeRenderer shapeRenderer = Main.getInstance().getShapeRenderer();
        shapeRenderer.setColor(0, 1, 0, 0.25f);
        shapeRenderer.rect(x, y, TILE_SIZE, TILE_SIZE);
    }



    public static class Builder {
        private Vector2 position = new Vector2(0, 0);
        private TileID id;
        private boolean collidable = false;

        public Builder position(Vector2 pos) {
            this.position = pos;
            return this;
        }

        public Builder id(TileID id) {
            this.id = id;
            return this;
        }

        public Builder collidable(boolean collidable) {
            this.collidable = collidable;
            return this;
        }

        public Tile build() {
            return new Tile(this);
        }
    }
}
