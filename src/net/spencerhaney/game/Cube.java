package net.spencerhaney.game;

import org.lwjgl.opengl.GL13;

import net.spencerhaney.engine.GameObject;
import net.spencerhaney.engine.Time;
import net.spencerhaney.opengl.Face;
import net.spencerhaney.opengl.GLUtil;
import net.spencerhaney.opengl.Vector3f;
import net.spencerhaney.opengl.Vertex;

public class Cube extends GameObject
{
    private Face[] face;
    private Vertex[] vertices;
    private float time;
    
    @Override
    public void init()
    {
        face = new Face[6];
        
        face[0] = new Face();
        face[1] = new Face();
        face[2] = new Face();
        face[3] = new Face();
        face[4] = new Face();
        face[5] = new Face();
        
        vertices = new Vertex[]
                {
                  new Vertex(-.5f, -.5f, .5f, 0, 10),
                  new Vertex(.5f, -.5f, .5f, 10, 10),
                  new Vertex(.5f, .5f, .5f, 10, 0),
                  new Vertex(-.5f, .5f, .5f, 0, 0),
                  new Vertex(-.5f, -.5f, -.5f, 0, 10),
                  new Vertex(.5f, -.5f, -.5f, 10, 10),
                  new Vertex(.5f, .5f, -.5f, 10, 0),
                  new Vertex(-.5f, .5f, -.5f, 0, 0),
                };
        int texture = GLUtil.createTexture("res\\images\\stone.png", GL13.GL_TEXTURE0);
        face[0].init(Vector3f.AWAY, texture, vertices[0], vertices[1], vertices[2], vertices[3]);
    }

    @Override
    public void update()
    {
        time += Time.getDelta() / 2;
        float s = (float)(Math.sin(time)) * 10;
        vertices = new Vertex[]
                {
                  new Vertex(-.5f, -.5f, .5f, 0, s),
                  new Vertex(.5f, -.5f, .5f, s, s),
                  new Vertex(.5f, .5f, .5f, s, 0),
                  new Vertex(-.5f, .5f, .5f, 0, 0),
                  new Vertex(-.5f, -.5f, -.5f, 0, s),
                  new Vertex(.5f, -.5f, -.5f, s, s),
                  new Vertex(.5f, .5f, -.5f, s, 0),
                  new Vertex(-.5f, .5f, -.5f, 0, 0),
                };
        face[0].update(Vector3f.AWAY, vertices[0], vertices[1], vertices[2], vertices[3]);
    }

    @Override
    public void render()
    {
       for(Face f : face)
       {
           f.render();
       }
    }
}
