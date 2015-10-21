package net.spencerhaney.engine;

import net.spencerhaney.opengl.Matrix4f;
import net.spencerhaney.opengl.Vector3f;

public abstract class GameObject
{
    public Vector3f position;
    public Vector3f speed;
    public Matrix4f rotation = new Matrix4f();
    private Game g;
    
    public abstract void update();

    /**
     * Initializes this object.
     * Is implicitly called when object is added to the game.
     */
    public abstract void init();
    
    public void render()
    {
        // Optional overriding; Do nothing
    }

    public void setGame(Game g)
    {
        this.g = g;
    }

    public Game getGame()
    {
        return g;
    }
}