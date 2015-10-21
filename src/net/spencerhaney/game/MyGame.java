package net.spencerhaney.game;

import net.spencerhaney.engine.Game;
import net.spencerhaney.opengl.Vector3f;

public class MyGame extends Game
{
    public MyGame()
    {
        super("Game");
    }

    @Override
    public void update()
    {

    }

    @Override
    public void render()
    {

    }

    @Override
    public void cleanup()
    {

    }

    @Override
    public void init()
    {
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                for (int k = 0; k < 20; k++)
                {
                    addObject(new Sphere(new Vector3f(i * 4, j * 4, k * 4)));
                }
            }
        }
    }
}