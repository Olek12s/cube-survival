package io.gith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class InputController implements Updatable
{

    public InputController() {
        Main.getInstance().getUpdatables().add(this);
    }

    public void update(float dt) {

    }
}
