package net.spencerhaney.opengl;

import java.util.Arrays;

public class Vertex
{
    // Vertex data
    private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
    private float[] rgba = new float[] {1f, 1f, 1f, 1f};
    private float[] st = new float[] {0f, 0f};
    
    /**
     * The number of elements/floats the Vertex has
     * x, y, z, w, r, g, b, a, s, t, normalx, normaly, normalz
     */
    public static final int ELEMENTS = 13;

    /**
     * The size of each element/float the Vertex has
     */
    public static final int SIZE = 4;

    /**
     * The number of bytes each Vertex contains
     */
    public static final int STRIDE = ELEMENTS * SIZE;

    /**
     * The number of bytes that offset the color
     */
    public static final int COLOR_OFFEST = 4 * SIZE;

    /**
     * The number of bytes that offset the texture coords
     */
    public static final int TEXTURE_OFFSET = 8 * SIZE;
    
    /**
     * The number of bytes that offset the normal values
     */
    public static final int NORMAL_OFFSET = 10 * SIZE;
    
    
    public Vertex(float x, float y, float z)
    {
        setXYZ(x, y, z);
    }
    
    public Vertex(float x, float y, float z, float s, float t)
    {
        setXYZ(x, y, z);
        setST(s, t);
    }
    
    public Vertex(float x, float y, float z, float r, float g, float b, float a)
    {
        setXYZ(x, y, z);
        setRGBA(r, g, b, a);
    }

    public Vertex(float x, float y, float z, float w, float r, float g, float b, float a)
    {
        setXYZW(x, y, z, w);
        setRGBA(r, g, b, a);
    }

    public Vertex(float x, float y, float z, float w, float r, float g, float b, float a, float s, float t)
    {
        setXYZW(x, y, z, w);
        setRGBA(r, g, b, a);
        setST(s, t);
    }
    
    public Vertex(float x, float y, float z, float r, float g, float b)
    {
        setXYZ(x, y, z);
        setRGB(r, g, b);
    }

    
    public Vertex()
    {
        // Do nothing
    }

    // Setters
    public void setXYZ(float x, float y, float z)
    {
        this.setXYZW(x, y, z, 1f);
    }

    public void setRGB(float r, float g, float b)
    {
        this.setRGBA(r, g, b, 1f);
    }

    public void setST(float s, float t)
    {
        this.st = new float[] {s, t};
    }
    
    public void setXYZW(float x, float y, float z, float w)
    {
        this.xyzw = new float[] {x, y, z, w};
    }

    public void setRGBA(float r, float g, float b, float a)
    {
        this.rgba = new float[] {r, g, b, 1f};
    }

    // Getters
    public float[] getXYZW()
    {
        return new float[] {this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3]};
    }

    public float[] getRGBA()
    {
        return new float[] {this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3]};
    }

    public float[] getST()
    {
        return new float[] {this.st[0], this.st[1]};
    }
    
    public float[] getElements(Vector3f normal)
    {
        float[] elements = new float[ELEMENTS];
        elements[0] = xyzw[0];
        elements[1] = xyzw[1];
        elements[2] = xyzw[2];
        elements[3] = xyzw[3];
        elements[4] = rgba[0];
        elements[5] = rgba[1];
        elements[6] = rgba[2];
        elements[7] = rgba[3];
        elements[8] = st[0];
        elements[9] = st[1];
        elements[10]= normal.getX();
        elements[11]= normal.getY();
        elements[12]= normal.getZ();
        return elements;
    }

    public float getX()
    {
        return xyzw[0];
    }
    
    public float getY()
    {
        return xyzw[1];
    }
    
    public float getZ()
    {
        return xyzw[2];
    }
    
    public float getW()
    {
        return xyzw[3];
    }
    
    public float getR()
    {
        return rgba[0];
    }
    
    public float getG()
    {
        return rgba[1];
    }
    
    public float getB()
    {
        return rgba[2];
    }
    
    public float getA()
    {
        return rgba[3];
    }
    
    public float getS()
    {
        return st[0];
    }
    
    public float getT()
    {
        return st[1];
    }
    
    public void setX(float x)
    {
        xyzw = new float[]{x, getY(), getZ(), getW()};
    }
    
    public void setY(float y)
    {
        xyzw = new float[]{getX(), y, getZ(), getW()};
    }
    
    public void setZ(float z)
    {
        xyzw = new float[]{getX(), getY(), z, getW()};
    }
    
    public void setW(float w)
    {
        xyzw = new float[]{getX(), getY(), getZ(), w};
    }
    
    public void setR(float r)
    {
        rgba = new float[]{r, getG(), getB(), getA()};
    }
    
    public void setG(float g)
    {
        rgba = new float[]{getR(), g, getB(), getA()};
    }
    
    public void setB(float b)
    {
        rgba = new float[]{getR(), getG(), b, getA()};
    }
    
    public void setA(float a)
    {
        rgba = new float[]{getR(), getG(), getB(), a};
    }
    
    public void setS(float s)
    {
        st = new float[]{s, getT()};
    }
    
    public void setT(float t)
    {
        st = new float[]{getS(), t};
    }
    
    @Override
    public String toString()
    {
        return Arrays.toString(xyzw) + Arrays.toString(rgba) + Arrays.toString(st);
    }
}
