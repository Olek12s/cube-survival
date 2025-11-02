package io.gith.entity.behavior;

import io.gith.Updatable;
import io.gith.entity.Entity;

public interface Behavior
{
    void start();
    void tick(float dt);
    void end();
    void setEntity(Entity entity);
}
