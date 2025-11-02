package io.gith.entity;

import io.gith.Updatable;

public interface Behavior
{
    void start();
    void tick(float dt);
    void end();
}
