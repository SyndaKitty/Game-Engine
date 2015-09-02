package net.spencerhaney.game;

import java.nio.file.Paths;

import net.spencerhaney.engine.GameObject;
import net.spencerhaney.opengl.Model;
import net.spencerhaney.opengl.OBJLoader;

public class Cube extends GameObject
{
    private Model model;

    @Override
    public void init()
    {
        model = OBJLoader.load(Paths.get("res\\models\\cube.obj"));
    }

    @Override
    public void update()
    {

    }

    @Override
    public void render()
    {
        model.render();
    }
}
