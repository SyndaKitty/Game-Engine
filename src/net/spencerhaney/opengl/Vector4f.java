package net.spencerhaney.opengl;

import java.util.Arrays;

public class Vector4f
{
    private float x;
    private float y;
    private float z;
    private float w;
    
    public Vector4f()
    {
        
    }

    public Vector4f(float x, float y, float z, float w)
    {
        setXYZW(x, y, z, w);
    }

    public void setXYZ(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public void setXYZW(float ... xyzw)
    {
        if(xyzw.length == 4)
        {
            x = xyzw[0];
            y = xyzw[1];
            z = xyzw[2];
            w = xyzw[3];
        }
        else
        {
            throw new IllegalArgumentException("Incorrect argument length of " + Arrays.toString(xyzw));
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

    public void setZ(float z)
    {
        this.z = z;
    }

    public void setW(float w)
    {
        this.w = w;
    }
    
    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }
    
    public float getW()
    {
        return w;
    }
    
    public float[] getXYZW()
    {
        return new float[]{x, y, z, w};
    }
    
    public String toString()
    {
        return Arrays.toString(new Float[]{x, y, z, w});
    }
}
