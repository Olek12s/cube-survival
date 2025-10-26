package io.gith.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.gith.Updatable;

public class EntityUpdater implements Updatable
{
    private Entity entity;

    public EntityUpdater(Entity entity) {
        this.entity = entity;
    }


    @Override
    public void update(float dt) {
        System.out.println("entity");
    }
}
