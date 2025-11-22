package io.gith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.gith.entity.entity.Player;
import io.gith.gui.GUI;
import io.gith.gui.Scale;

public class InputController implements Updatable
{

    public InputController() {
        Main.getInstance().getUpdatables().add(this);
    }

    public void update(float dt) {
        checkOpenInventory();
        checkSetGUIScale();
    }

    private void checkOpenInventory()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E))
        {
            ((Player)Main.getInstance().getPlayer()).getGui().getInventoryUI().toggle();
        }
    }

    private void checkSetGUIScale() {
        // --- SCALE SMALL --- //
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            GUI.setScale(Scale.SMALL);
            System.out.println("\u001B[33mGUI:\u001B[0m Toggled GUI SCALE: \u001B[32m" + GUI.getScale() + "\u001B[0m");
        }

        // --- SCALE MEDIUM --- //
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GUI.setScale(Scale.MEDIUM);
            System.out.println("\u001B[33mGUI:\u001B[0m Toggled GUI SCALE: \u001B[32m" + GUI.getScale() + "\u001B[0m");
        }

        // --- SCALE LARGE --- //
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            GUI.setScale(Scale.LARGE);
            System.out.println("\u001B[33mGUI:\u001B[0m Toggled GUI SCALE: \u001B[32m" + GUI.getScale() + "\u001B[0m");
        }
    }

}
