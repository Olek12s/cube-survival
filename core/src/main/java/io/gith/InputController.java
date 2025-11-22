package io.gith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import io.gith.entity.entity.Player;

public class InputController implements Updatable
{

    public InputController() {
        Main.getInstance().getUpdatables().add(this);
    }

    public void update(float dt) {
        checkOpenInventory();
    }

    private void checkOpenInventory()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E))
        {
            ((Player)Main.getInstance().getPlayer()).getInventoryUI().toggle();
        }
    }
}
