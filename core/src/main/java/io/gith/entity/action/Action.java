package io.gith.entity.action;

import io.gith.entity.entity.Entity;

public interface Action
{
    void start();
    void tick(float dt);
    void end();
    void setEntity(Entity entity);
}
