package io.gith;

public interface Spawnable extends Updatable, Renderable
{
    Renderable getRenderer();
    Updatable getUpdater();

    default void spawn() {
        if (isSpawned()) return;
        Main.getInstance().getRenderables().add(this);
        Main.getInstance().getUpdatables().add(this);
        setSpawned(true);
    }
    default void despawn() {
        if (!isSpawned()) return;
        Main.getInstance().getRenderables().remove(getRenderer());
        Main.getInstance().getUpdatables().remove(getUpdater());
        setSpawned(false);
    }

    boolean isSpawned();
    void setSpawned(boolean value);
}
