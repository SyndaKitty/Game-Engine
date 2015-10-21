package net.spencerhaney.game;

import java.nio.file.Paths;

import net.spencerhaney.engine.GameObject;
import net.spencerhaney.engine.Time;
import net.spencerhaney.opengl.Matrix4f;
import net.spencerhaney.opengl.Model;
import net.spencerhaney.opengl.OBJLoader;
import net.spencerhaney.opengl.Vector3f;

public class Sphere extends GameObject
{
    private Model model;

    private float xrot;
    private float yrot;
    private float zrot;
    private float srot;

    public Sphere(Vector3f position)
    {
        this.position = position;
    }

    @Override
    public void init()
    {
        model = OBJLoader.load(Paths.get("res/models/sphere.obj"));
        xrot = (float)Math.random();
        yrot = (float)Math.random();
        zrot = (float)Math.random();
        srot = (float)Math.random() * 50 + 10;
    }

    @Override
    public void update()
    {
        rotation = rotation.multiply(Matrix4f.rotate(Time.getDelta() * srot, xrot, yrot, zrot));
    }

    @Override
    public void render()
    {
        model.render(position, rotation);
    }
}
