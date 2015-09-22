package net.spencerhaney.engine;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import net.spencerhaney.opengl.GLUtil;

public class ScreenManager
{
    private long window;
    private int width;
    private int height;

    public void createFullWindow(final String title)
    {
        // Configure our window
        GLFW.glfwDefaultWindowHints(); // Defaults
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE); // Stay hidden after creation

        // Get the resolution of the primary monitor
        final ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        width = GLFWvidmode.width(vidmode);
        height = GLFWvidmode.height(vidmode);

        // Create the window
        window = GLFW.glfwCreateWindow(width, height, title, GLFW.glfwGetPrimaryMonitor(),
                NULL);
        if (window == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW Window");
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        GLContext.createFromCurrent();
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.2f, 0.2f, 0.4f, 1.0f);
        GLUtil.init();
    }

    public void createWindow(final String title, final int width, final int height)
    {
        this.width = width;
        this.height = height;
        // Configure our window
        GLFW.glfwDefaultWindowHints(); // Defaults
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE); // Stay hidden after creation
        
        //We want a forward compatible 3.2 OpenGL context
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
        
        // Create the window
        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
        {
            Logging.info("OpenGL 3.2 unavailable");
            GLFW.glfwDefaultWindowHints(); // Defaults
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE); // Stay hidden after creation
            
            //Attempt to use OpenGL 2.1
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
            
            //Create the window
            GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
            
            if(window == NULL)
            {
                Logging.severe("Could not use OpenGL 3.2 or 2.1; Update you graphics card");
                EngineManager.errorStop(ErrorCodes.GLFW_CREATE_WINDOW_FAIL);
            }
            
            GLUtil.setLegacy(true);
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        GLContext.createFromCurrent();
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.2f, 0.2f, 0.4f, 1.0f);
        GLUtil.init();
    }

    public void setKeyCallback(GLFWKeyCallback callback)
    {
        GLFW.glfwSetKeyCallback(window, callback);
    }

    public boolean isOpen()
    {
        return GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE;
    }

    public void update()
    {
        // Swap the color buffers
        GLFW.glfwSwapBuffers(window);

        // Clear the framebuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Poll window for events (eg: key callback)
        GLFW.glfwPollEvents();
    }

    public void show()
    {
        GLFW.glfwShowWindow(window);
    }

    public long getWindow()
    {
        return window;
    }
}
