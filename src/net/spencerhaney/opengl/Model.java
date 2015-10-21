package net.spencerhaney.opengl;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Model
{
    private List<Vector3f> vertices;
    private int vao;
    private int vertexVbo;
    private int uvVbo;
    private int normalVbo;
    
    public void init(List<Vector3f> vertices, List<Vector2f> uvs, List<Vector3f> normals)
    {
        this.vertices = vertices;
        
     // Generate and bind VAO
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Generate and bind vertex VBO
        vertexVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, GLUtil.getBuffer3f(vertices), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        
        uvVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, GLUtil.getBuffer2f(uvs), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        
        normalVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, GLUtil.getBuffer3f(normals), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
        
    }

    public void render(Vector3f position, Matrix4f rotation)
    {
        Matrix4f translation = Matrix4f.translate(position);
        GLUtil.uploadMVP(translation.multiply(rotation));
        
        GL20.glUseProgram(GLUtil.program);
        GL30.glBindVertexArray(vao);
        
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size());
        
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        
        GL20.glUseProgram(GLUtil.program);
    }
}