package io.gith.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import io.gith.utils.Pair;

public class Packer
{
    public static void main(String[] args) {
        TexturePacker.process("assets/tiles", "assets", "tiles");
        TexturePacker.process("assets/entities", "assets", "entities");

    }
}
