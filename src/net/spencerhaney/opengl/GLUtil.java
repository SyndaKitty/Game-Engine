package net.spencerhaney.opengl;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import net.spencerhaney.engine.ErrorCodes;
import net.spencerhaney.engine.Logging;
import net.spencerhaney.engine.Resources;

public class GLUtil
{
    public static int vertexShader;
    public static int fragmentShader;
    public static int textureFragmentShader;
    public static int program;
    public static int textureProgram;
    
    private static ArrayList<Integer> loadedTextures = new ArrayList<Integer>();
    
    private GLUtil()
    {
        // Do nothing
    }

    public static void init()
    {
        Logging.fine("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        String vShader = loadFile(Paths.get("GLSL/vertexShader.glsl"));
        vertexShader = createShader(GL20.GL_VERTEX_SHADER, vShader);

        String fShader = loadFile(Paths.get("GLSL/fragmentShader.glsl"));
        fragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, fShader);
        
        String tShader = loadFile(Paths.get("GLSL/textureFragmentShader.glsl"));
        textureFragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, tShader);
        
        program = createProgram(vertexShader, fragmentShader);
        textureProgram = createProgram(vertexShader, textureFragmentShader);
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
        GL20.glDetachShader(program, textureFragmentShader);
        
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        GL20.glDeleteShader(textureFragmentShader);
        
        GL20.glDeleteProgram(program);
        GL20.glDeleteProgram(textureProgram);
    }

    public static int createTexture(final String fileName, final int textureUnit)
    {
        Object[] textureResource = Resources.getResource(fileName);
        
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
            Logging.severe("Missing texture: " + fileName, e);
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

        return texture;
    }

    public static int createProgram(final int... shaders)
    {
        int program = GL20.glCreateProgram();
        //Position information will be attribute 0
        GL20.glBindAttribLocation(program, 0, "in_Position");
        // Color information will be attribute 1
        GL20.glBindAttribLocation(program, 1, "in_Color");
        // Texture coord information will be attribute 2
        GL20.glBindAttribLocation(program, 2, "in_TextureCoord");
        // Normal vector will be attribute 3
        GL20.glBindAttribLocation(program, 3, "in_Normal");        
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

    public static int createProgram(final int[] shaderTypes, final String[] shaders)
    {
        int[] shaderIds = new int[shaders.length];
        for (int i = 0; i < shaderIds.length; i++)
        {
            shaderIds[i] = createShader(shaderTypes[i], shaders[i]);
        }
        return createProgram(shaderIds);
    }

    public static int createShader(final int shadertype, final String shaderString)
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

    public static String loadFile(final Path p)
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
