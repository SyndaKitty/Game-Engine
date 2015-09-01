package net.spencerhaney.engine;

import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

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
        glfwDefaultWindowHints(); // Defaults
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Stay hidden after creation

        // Get the resolution of the primary monitor
        final ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        width = GLFWvidmode.width(vidmode);
        height = GLFWvidmode.height(vidmode);

        // Create the window
        window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
        if (window == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW Window");
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        GLContext.createFromCurrent();
        GL11.glViewport(0, 0, width, height);
        glClearColor(0.2f, 0.2f, 0.4f, 1.0f);
        GLUtil.init();
    }

    public void setKeyCallback(GLFWKeyCallback callback)
    {
        glfwSetKeyCallback(window, callback);
    }

    public boolean isOpen()
    {
        return glfwWindowShouldClose(window) == GL_FALSE;
    }

    public void update()
    {   
        // Swap the color buffers
        glfwSwapBuffers(window);

        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Poll window for events (eg: key callback)
        glfwPollEvents();
    }

    public void show()
    {
        glfwShowWindow(window);
    }

    public long getWindow()
    {
        return window;
    }
}
