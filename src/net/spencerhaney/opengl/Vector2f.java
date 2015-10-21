package net.spencerhaney.opengl;

import java.util.Arrays;

public class Vector2f
{
    public static final Vector2f UP = new Vector2f(0f, 1f);
    public static final Vector2f DOWN = new Vector2f(0f, -1f);
    public static final Vector2f RIGHT = new Vector2f(1f, 0f);
    public static final Vector2f LEFT = new Vector2f(-1f, 0f);

    private float x;
    private float y;

    public Vector2f()
    {

    }

    public Vector2f(float x, float y)
    {
        setXY(x, y);
    }

    public Vector2f add(Vector2f other)
    {
        return new Vector2f(x + other.x, y + other.y);
    }
    
    public Vector2f subtract(Vector2f other)
    {
        return new Vector2f(x - other.x, y - other.y);
    }
    
    public Vector2f multiply(float scalar)
    {
        return new Vector2f(scalar * x, scalar * y);
    }
    
    public Vector2f negate()
    {
        return this.multiply(-1);
    }
    
    public void setXY(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setXY(float[] xy)
    {
        if (xy.length == 2)
        {
            x = xy[0];
            y = xy[1];
        }
        else
        {
            throw new IllegalArgumentException("Incorrect argument length of " + Arrays.toString(xy));
        }
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float dot(Vector2f other)
    {
        return x * other.x + y * other.y;
    }
    
    public float length()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public float lengthSquared()
    {
        return x * x + y * y;
    }

    public Vector2f normalize()
    {
        float length = length();
        return new Vector2f(x / length, y / length);
    }

    public float[] getXY()
    {
        return new float[] {x, y};
    }

    public String toString()
    {
        return Arrays.toString(new Float[] {x, y});
    }
}