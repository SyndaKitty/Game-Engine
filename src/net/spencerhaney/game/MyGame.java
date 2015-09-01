package net.spencerhaney.game;

import net.spencerhaney.engine.Game;

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
        addObject(new Cube());
    }
}