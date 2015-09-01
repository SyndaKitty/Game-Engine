package net.spencerhaney.engine;

public class ErrorCodes
{
    /**
     * The Error code that occurs when shader complilation cannot be completed successfully
     */
    public static final int SHADER_COMPILATION = 1;
    
    /**
     * The Error code that occurs when attempting to use a texture that has not been located by the Resource manager
     */
    public static final int MISSING_TEXTURE = 2;
    
    /**
     * The Error code that occurs when attempting to create a polygon with too many sides
     */
    public static final int POLYGON_SIDES_TOO_LARGE = 3;
    
    /**
     * The Error code that occurs when updating a face with more or less vertices than it had
     */
    public static final int FACE_VERTICES_SIZE_MISTMATCH = 4;
}