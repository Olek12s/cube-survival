package io.gith;

public interface Spawnable extends Updatable, Renderable
{
    Renderable getRenderer();
    Updatable getUpdater();

    default void spawn() {
        if (isSpawned()) return;
        setSpawned(true);

        Main m = Main.getInstance();
        m.getToAddRenderables().add(this);
        m.getToAddUpdatables().add(this);
    }

    default void despawn() {
        if (!isSpawned()) return;
        setSpawned(false);

        Main m = Main.getInstance();
        m.getToRemoveRenderables().add(this);
        m.getToRemoveUpdatables().add(this);
    }


    boolean isSpawned();
    void setSpawned(boolean value);
}
