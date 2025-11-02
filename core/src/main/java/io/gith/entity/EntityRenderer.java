package io.gith.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.gith.Main;
import io.gith.Renderable;
import io.gith.utils.Direction;

import java.util.EnumMap;
import java.util.Map;

public class EntityRenderer implements Renderable
{
    private final Map<Direction, Animation<TextureRegion>> walkAnimations;
    private float stateTime = 0f;
    private Entity entity;


    public EntityRenderer(Entity entity) {
        this.entity = entity;
        this.walkAnimations = new EnumMap<>(Direction.class);

        TextureRegion region = Main.getInstance().getAssetsController().getEntityRegion(entity.id.getEntityName());

        if (region == null) {
            throw new IllegalStateException("No texture found for entity: " + entity.getId().getEntityName());
        }

        int frameSize = 16;
        TextureRegion[][] frames = region.split(frameSize, frameSize);

        walkAnimations.put(Direction.UP, new Animation<>(0.2f, frames[0]));          // row 0 = UP
        walkAnimations.put(Direction.DOWN, new Animation<>(0.2f, frames[1]));          // row 1 = DOWN
        walkAnimations.put(Direction.LEFT, new Animation<>(0.2f, frames[2]));         // row 2 = LEFT
        walkAnimations.put(Direction.RIGHT, new Animation<>(0.2f, frames[3]));            // row 3 = RIGHT
    }

    @Override
    public void renderTexture() {
        SpriteBatch batch = Main.getInstance().getBatch();
        stateTime += Gdx.graphics.getDeltaTime();

        Direction currentDirection = entity.getDirection();
        Animation<TextureRegion> anim = walkAnimations.get(currentDirection);

        TextureRegion frame;
        if (!entity.isWalking) {
            frame = anim.getKeyFrame(0, true);
        }
        else {
            frame = anim.getKeyFrame(stateTime, true);
        }
        batch.draw(frame, entity.getWorldPosition().x, entity.getWorldPosition().y);
    }


    @Override
    public void renderShape() {
        ShapeRenderer shapeRenderer = Main.getInstance().getShapeRenderer();

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.polygon(entity.getHitbox().getPolygon().getTransformedVertices());
    }
}
