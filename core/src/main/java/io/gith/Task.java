package io.gith;

public interface Task
{
    void start();
    void updateTask(float dt);
    void end();
    boolean isActive();
}
