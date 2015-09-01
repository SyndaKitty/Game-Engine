package net.spencerhaney.engine;

public abstract class GameObject
{
    // private float x;
    // private float y;
    // private float z;
    private Game g;

    public abstract void update();

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
