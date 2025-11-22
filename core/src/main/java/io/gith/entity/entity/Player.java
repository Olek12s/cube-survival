package io.gith.entity.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.gith.Main;
import io.gith.gui.GUI;
import io.gith.gui.InventoryRenderer;
import io.gith.gui.InventoryUI;
import lombok.Getter;

@Getter
public class Player extends Entity
{
    private final Vector2 desiredDirection = new Vector2();
    private final GUI gui;

    public Player(Vector2 startPosition) {
        super(EntityID.PLAYER, startPosition);

        InventoryRenderer renderer = new InventoryRenderer(inventory);
        Main.getInstance().getRenderablesGUI().add(renderer);
        this.gui = new GUI(this);
        speed = 120f;
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        super.update(dt);
    }

    @Override
    public Vector2 getMovementInput() {
        return desiredDirection.cpy();
    }


    private void handleInput(float dt) {
        desiredDirection.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) desiredDirection.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) desiredDirection.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) desiredDirection.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) desiredDirection.x += 1;

        if (desiredDirection.len2() > 0) {
            desiredDirection.nor();
        }
    }
}
