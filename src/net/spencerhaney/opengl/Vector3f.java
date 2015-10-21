package net.spencerhaney.opengl;

import java.util.Arrays;

public class Vector3f
{
    public static final Vector3f UP = new Vector3f(0f, 1f, 0f);
    public static final Vector3f DOWN = new Vector3f(0f, -1f, 0f);
    public static final Vector3f RIGHT = new Vector3f(1f, 0f, 0f);
    public static final Vector3f LEFT = new Vector3f(-1f, 0f, 0f);

    /**
     * The normalized vector that points toward the camera.
     */
    public static final Vector3f TOWARD = new Vector3f(0f, 0f, 1f);

    /**
     * The normalized vector that points away from the camera.
     */
    public static final Vector3f AWAY = new Vector3f(0f, 0f, -1f);

    private float x;
    private float y;
    private float z;

    public Vector3f()
    {

    }

    public Vector3f(float x, float y, float z)
    {
        setXYZ(x, y, z);
    }

    public Vector3f add(Vector3f other)
    {
        return new Vector3f(x + other.x, y + other.y, z + other.z);
    }
    
    public Vector3f subtract(Vector3f other)
    {
        return new Vector3f(x - other.x, y - other.y, z - other.z);
    }
    
    public Vector3f multiply(float scalar)
    {
        return new Vector3f(scalar * x, scalar * y, scalar * z);
    }
    
    public Vector3f negate()
    {
        return this.multiply(-1);
    }
    
    public Vector3f cross(Vector3f other)
    {
        float x = this.y * other.z - this.z * other.y;
        float y = this.z * other.x - this.x * other.z;
        float z = this.x * other.y - this.y * other.x;
        
        return new Vector3f(x, y, z);
    }
    
    public void setXYZ(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setXYZ(float[] xyz)
    {
        if (xyz.length == 3)
        {
            x = xyz[0];
            y = xyz[1];
            z = xyz[2];
        }
        else
        {
            throw new IllegalArgumentException("Incorrect argument length of " + Arrays.toString(xyz));
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

    public float dot(Vector3f other)
    {
        return x * other.x + y * other.y + z * other.z;
    }
    
    public float length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    public Vector3f normalize()
    {
        float length = length();
        return new Vector3f(x / length, y / length, z / length);
    }

    public float[] getXYZ()
    {
        return new float[] {x, y, z};
    }

    public String toString()
    {
        return Arrays.toString(new Float[] {x, y, z});
    }
}
