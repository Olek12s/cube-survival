package io.gith.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.gith.CameraController;
import io.gith.tile.Tile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*
-Only convex shapes are supported
 */
public class Hitbox
{
    private final Polygon polygon;

    public Hitbox(float[] vertices)
    {
        if (vertices.length < 6) {
            throw new IllegalArgumentException("Vertices count is too small to create convex shape.");
        }
        this.polygon = new Polygon(vertices);
    }

    public void setPosition(float x, float y) {polygon.setPosition(x, y);}

    public Rectangle getBoundingBoxAABB() {
        return polygon.getBoundingRectangle();
    }
    public Vector2 getMiddlePoint() {
        Rectangle aabb = getBoundingBoxAABB();
        return new Vector2(
            aabb.x + aabb.width / 2f,
            aabb.y + aabb.height / 2f
        );
    }


    public boolean overlaps(Hitbox other) {
        return Intersector.overlapConvexPolygons(this.polygon, other.polygon);
    }

    public boolean overlaps(Polygon other) {
        return Intersector.overlapConvexPolygons(this.polygon, other);
    }

    public boolean overlapsTile(Tile tile) {

        if (tile == null) throw new IllegalArgumentException("Tile is null");
        if (!tile.isCollidable()) return false;

        float worldX = tile.getWorldX();
        float worldY = tile.getWorldY();

        float[] tileVertices = new float[]{
            worldX, worldY,
            worldX + CameraController.TILE_SIZE, worldY,
            worldX + CameraController.TILE_SIZE, worldY + CameraController.TILE_SIZE,
            worldX, worldY + CameraController.TILE_SIZE
        };
        Polygon tilePoly = new Polygon(tileVertices);

        return Intersector.overlapConvexPolygons(this.polygon, tilePoly);
    }

    public Hitbox copy() {
        float[] original = polygon.getVertices();


        float[] vertsCopy = new float[original.length];
        System.arraycopy(original, 0, vertsCopy, 0, original.length);

        Hitbox h = new Hitbox(vertsCopy);


        h.getPolygon().setPosition(
            polygon.getX(),
            polygon.getY()
        );

        h.getPolygon().setRotation(polygon.getRotation());
        h.getPolygon().setScale(polygon.getScaleX(), polygon.getScaleY());

        return h;
    }


}
