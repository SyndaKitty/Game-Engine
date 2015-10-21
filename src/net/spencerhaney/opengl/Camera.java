package net.spencerhaney.opengl;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.spencerhaney.engine.Time;

public class Camera
{
    private static final float TWO_PI = (float)Math.PI * 2;
    private static final float HALF_PI = (float)(Math.PI * 0.5) - 0.01f;
    private static final float Z_OFFSET = 1.5f;
    private static final float INIT_FOV = 45f;
    private static final float INIT_NEAR = 0.1f;
    private static final float INIT_FAR = 500f;
    private static final float FP_SENSITIVITY = TWO_PI / 200.0f;
    private static Matrix4f projectionMatrix;
    private static Matrix4f viewMatrix = new Matrix4f();
    private static Vector3f position = new Vector3f();
    private static float horizontal;
    private static float vertical;

    public static void init(final float aspectRatio)
    {
        position.setXYZ(0, 0, Z_OFFSET);
        float fieldOfView = INIT_FOV;
        float near_plane = INIT_NEAR;
        float far_plane = INIT_FAR;
        projectionMatrix = Matrix4f.perspective(fieldOfView, aspectRatio, near_plane, far_plane);
    }

    public static Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }

    public static Matrix4f getViewMatrix()
    {
        return viewMatrix;
    }

    public static void fpsCamera(long windowHandle)
    {
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(windowHandle, xpos, ypos);
        GLFW.glfwSetCursorPos(windowHandle, 1920 / 2, 1080 / 2); //TODO remove hardcoding
        
        horizontal += FP_SENSITIVITY * Time.getDelta() * (float)(1920/2 - xpos.get());
        vertical += FP_SENSITIVITY * Time.getDelta() * (float)(1080/2 - ypos.get());
        
        //Clamp horizontal to [0,2pi)
        while(horizontal >= TWO_PI)
        {
            horizontal = horizontal - TWO_PI;
        }
        while(horizontal < 0)
        {
            horizontal += TWO_PI;
        }
        //Clamp vertical to (-pi/2, pi/2]
        if(vertical > HALF_PI)
        {
            vertical = HALF_PI;
        }
        while(vertical < -HALF_PI)
        {
            vertical = -HALF_PI;
        }
        
        float cv = (float)Math.cos(-vertical);
        float ch = (float)Math.cos(horizontal);
        float sv = (float)Math.sin(-vertical);
        float sh = (float)Math.sin(horizontal);
        Vector3f direction = new Vector3f(cv * sh, -sv, cv * ch);
        float speed = 5;
        
        Vector3f left = new Vector3f((float)Math.sin(horizontal - Math.PI / 2), 0, (float)Math.cos(horizontal - Math.PI / 2));
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS)
        {
            position = position.add(direction.multiply(Time.getDelta() * speed));
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS)
        {
            position = position.subtract(direction.multiply(Time.getDelta() * speed));
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS)
        {
            position = position.add(left.multiply(Time.getDelta() * speed));
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS)
        {
            position = position.subtract(left.multiply(Time.getDelta() * speed));
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS)
        {
            horizontal = vertical = 0;
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS)
        {
            position = new Vector3f();
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS)
        {
            position = position.add(new Vector3f(0, 1, 0).multiply(Time.getDelta() * speed));
        }
        if(GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS)
        {
            position = position.add(new Vector3f(0, -1, 0).multiply(Time.getDelta() * speed));
        }
        viewMatrix = Matrix4f.lookAt(position, position.add(direction), Vector3f.UP);
    }
}
