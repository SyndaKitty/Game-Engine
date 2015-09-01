package net.spencerhaney.engine;

import java.util.ArrayList;

public abstract class Game
{
    private String title;

    private final ArrayList<GameObject> objects = new ArrayList<GameObject>();

    public Game(String title)
    {
        this.title = title;
    }

    public final void gameUpdate()
    {
        update();
        for (GameObject o : objects)
        {
            o.update();
        }
    }

    public final void gameRender()
    {
        render();
        for (GameObject o : objects)
        {
            o.render();
        }
    }

    public String getTitle()
    {
        return title;
    }

    public abstract void init();
    
    public abstract void update();

    public abstract void render();

    public abstract void cleanup();

    public void addObject(GameObject o)
    {
        o.setGame(this);
        o.init();
        objects.add(o);
    }
}