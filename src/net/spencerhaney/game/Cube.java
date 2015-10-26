package net.spencerhaney.game;

import java.nio.file.Paths;

import net.spencerhaney.engine.GameObject;
import net.spencerhaney.opengl.Model;
import net.spencerhaney.opengl.ModelLoader;
import net.spencerhaney.opengl.Vector3f;

public class Cube extends GameObject
{
    private Model model;

    public Cube()
    {
        this(new Vector3f());
    }
    
    public Cube(Vector3f position)
    {
        this.position = position;
    }

    @Override
    public void init()
    {
        model = ModelLoader.load(Paths.get("res\\models\\TexturedCube.obj"), Paths.get("res\\images\\dirt.png"));
    }

    @Override
    public void update()
    {
        
    }

    @Override
    public void render()
    {
        model.render(position, rotation);
    }
}