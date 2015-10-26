package net.spencerhaney.opengl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.spencerhaney.engine.EngineManager;
import net.spencerhaney.engine.ErrorCodes;
import net.spencerhaney.engine.Logging;

public class GLUtil
{
    public static int vertexShader;
    public static int fragmentShader;
    public static int program;
    
    private static boolean legacy;
    
    private static ArrayList<Integer> loadedTextures = new ArrayList<Integer>();
    private static int matrixLocation;
    
    private static Map<String, Integer> textures = new HashMap<>();
    
    private GLUtil()
    {
        // Do nothing
    }

    public static void init()
    {
        Logging.fine("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        String vShader = loadFile(Paths.get("GLSL/vertexShader.glsl"));
        vertexShader = createShader(GL20.GL_VERTEX_SHADER, vShader);

        String fShader = loadFile(Paths.get("GLSL/fragmentShader.glsl"));
        fragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, fShader);
        
        program = createProgram(vertexShader, fragmentShader);
        
        matrixLocation = GL20.glGetUniformLocation(program, "MVP");
    }

    /**
     * Uploads the MVP matrix to OpenGL
     */
    public static void uploadMVP(Matrix4f model)
    {
        GL20.glUseProgram(program);
        Matrix4f mvp = Camera.getProjectionMatrix();
        mvp = mvp.multiply(Camera.getViewMatrix());
        mvp = mvp.multiply(model);
        FloatBuffer buffer = mvp.getBuffer();
        GL20.glUniformMatrix4fv(matrixLocation, false, buffer);
        GL20.glUseProgram(0);
    }
    
    public static void cleanup()
    {
        //Delete the textures
        for(int texture : loadedTextures)
        {
            GL11.glDeleteTextures(texture);
        }
        
        //Delete the shaders
        GL20.glUseProgram(program);
        
        GL20.glDetachShader(program, vertexShader);
        GL20.glDetachShader(program, fragmentShader);
        
        GL20.glUseProgram(program);
        GL20.glDetachShader(program, vertexShader);
        
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        
        GL20.glDeleteProgram(program);
    }

    public static int createTexture(final Path p, final int textureUnit)
    {
        if(textures.containsKey(p.normalize().toString()))
        {
            return textures.get(p.normalize().toString());
        }
        Object[] textureResource = loadPNG(p);
        
        ByteBuffer textureBytes = null;
        int width = 0;
        int height = 0;
        
        try
        {
            textureBytes = (ByteBuffer)textureResource[0];
            width = (int)textureResource[1];
            height = (int)textureResource[2];
        }
        catch (NullPointerException | ClassCastException | ArrayIndexOutOfBoundsException e)
        {
            Logging.severe("Missing texture: " + p.toString(), e);
            System.exit(ErrorCodes.MISSING_TEXTURE);
        }

        // Create a new texture object in memory and bind it
        int texture = GL11.glGenTextures();
        GL13.glActiveTexture(textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
         
        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
         
        // Upload the texture data and generate mip maps (for scaling)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, 
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureBytes);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
         
        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
         
        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                GL11.GL_LINEAR_MIPMAP_LINEAR);
        textures.put(p.normalize().toString(), texture);
        
        return texture;
    }

    public static FloatBuffer getBuffer3f(List<Vector3f> vectors)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vectors.size() * 3 * Float.SIZE);
        for(int i = 0; i < vectors.size(); i++)
        {
            buffer.put(vectors.get(i).getXYZ());
        }
        buffer.flip();
        return buffer;
    }
    
    public static FloatBuffer getBuffer2f(List<Vector2f> vectors)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vectors.size() * 2 * Float.SIZE);
        for(int i = 0; i < vectors.size(); i++)
        {
            buffer.put(vectors.get(i).getXY());
        }
        buffer.flip();
        return buffer;
    }
    
    public static void setLegacy(boolean b)
    {
        legacy = b;
    }
    
    public static boolean isLegacy()
    {
        return legacy;
    }
    
    public static void setWireframeMode(final boolean on)
    {
        if (on)
        {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }
        else
        {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }
    }

    private static Object[] loadPNG(Path p)
    {
        String filePath = p.toString();
        
        InputStream in = null;
        try
        {
            in = new FileInputStream(filePath);
        }
        catch (FileNotFoundException e)
        {
            Logging.severe(e);
            EngineManager.errorStop(ErrorCodes.MISSING_TEXTURE);
        }
        PNGDecoder decoder = null;
        try
        {
            decoder = new PNGDecoder(in);
        }
        catch (IOException e1)
        {
            Logging.severe(e1);
            EngineManager.errorStop(ErrorCodes.INCORRECT_TEXTURE);
        }

        Logging.fine("Loading \"" + filePath + "\" - " + decoder.getWidth() + "x" + decoder.getHeight() + "px");

        final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        try
        {
            decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        buffer.flip();
        return new Object[]{buffer, decoder.getWidth(), decoder.getHeight()};
    }
    
    private static int createProgram(final int... shaders)
    {
        int program = GL20.glCreateProgram();
        //Position information will be attribute 0
        GL20.glBindAttribLocation(program, 0, "in_Position");
        // Texture coord information will be attribute 1
        GL20.glBindAttribLocation(program, 1, "in_TextureCoord");
        // Normal vector will be attribute 2
        GL20.glBindAttribLocation(program, 2, "in_Normal");        
        for (int s : shaders)
        {
            GL20.glAttachShader(program, s);
        }
        
        GL20.glLinkProgram(program);
        int status = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
        if (status == GL11.GL_FALSE)
        {
            String error = GL20.glGetProgramInfoLog(program);
            Logging.severe(String.format("Linker failure: %s\n", error));
        }
        return program;
    }
    
    private static int createShader(final int shadertype, final String shaderString)
    {
        int shader = GL20.glCreateShader(shadertype);
        GL20.glShaderSource(shader, shaderString);
        GL20.glCompileShader(shader);
        int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (status == GL11.GL_FALSE)
        {
            String error = GL20.glGetShaderInfoLog(shader);

            String shaderTypeString = null;
            switch (shadertype)
            {
                case GL20.GL_VERTEX_SHADER:
                    shaderTypeString = "vertex";
                    break;
                case GL32.GL_GEOMETRY_SHADER:
                    shaderTypeString = "geometry";
                    break;
                case GL20.GL_FRAGMENT_SHADER:
                    shaderTypeString = "fragment";
                    break;
            }
            Logging.severe(String.format("Compile failure in %s shader:\n%s\n", shaderTypeString, error));
            System.exit(ErrorCodes.SHADER_COMPILATION);
        }
        return shader;
    }

    private static String loadFile(final Path p)
    {
        Scanner in = null;
        try
        {
            in = new Scanner(p.toFile());
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        while (in.hasNextLine())
        {
            buffer.append(in.nextLine() + "\n");
        }
        return buffer.toString();
    }
}