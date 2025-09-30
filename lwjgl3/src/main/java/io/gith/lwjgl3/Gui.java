package io.gith.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.ImGui;
import io.gith.Main;
import io.gith.Renderable;


public class Gui implements Renderable
{
    private static ImGuiImplGlfw imGuiGlfw;
    private static ImGuiImplGl3 imGuiGl3;
    private static InputProcessor tmpProcessor;


    public Gui() {
        Main.getInstance().getRenderables().add(this);
        init();
    }

    @Override
    public void render() {
        float windowWidth = Gdx.graphics.getWidth() / 5f;

        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(windowWidth, 0, ImGuiCond.Always);
        int windowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize;
        ImGui.begin("dbg", windowFlags);
        ImGui.text(String.format("FPS: %.1f | UPS: %.1f", Main.currentFPS, Main.currentUPS));
        ImGui.text(String.format("camera X: %.3f", Main.getInstance().getCameraController().getCamera().position.x));
        ImGui.text(String.format("camera Y: %.3f",  Main.getInstance().getCameraController().getCamera().position.y));
        ImGui.end();
    }


    public void init() {
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();

        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.getFonts().addFontDefault();
        io.getFonts().build();

        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 150");
    }

    public static void startFrame() {
        if (tmpProcessor != null) {
            Gdx.input.setInputProcessor(tmpProcessor);
            tmpProcessor = null;
        }
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public static void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().getWantCaptureKeyboard() || ImGui.getIO().getWantCaptureMouse()) {
            tmpProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(null);
        }
    }

    public void dispose() {
        imGuiGl3 = null;
        imGuiGlfw = null;
        ImGui.destroyContext();
    }
}
