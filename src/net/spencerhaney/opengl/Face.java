package net.spencerhaney.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.spencerhaney.engine.EngineManager;
import net.spencerhaney.engine.ErrorCodes;
import net.spencerhaney.engine.Logging;

public class Face
{
    private static final int MAX_SIDES = Byte.MAX_VALUE - Byte.MIN_VALUE;
    
    private int vao;
    private int vbo;

    private FloatBuffer combinedBuffer;

    private int ivbo;
    private ByteBuffer indicesBuffer;
    private int indicesLength;
    
    private Vector3f normal;
    private Vertex[] vertices;
    private Integer texture;
    
    public void init(Vector3f normal, int texture, Vertex ... vertices)
    {
        this.texture = texture;
        init(normal, vertices);
    }
    
    /**
     * Create the necessary OpenGL components to render this Geometry
     * 
     * @param vertices
     *            The vertices in counter-clockwise order
     */
    public void init(Vector3f normal, Vertex... vertices)
    {
        this.normal = normal;
        this.vertices = vertices.clone();
        
        // Setup vertices
        setVertices(vertices);

        // Setup indices
        byte[] indices = indexSequence(vertices.length);
        indicesLength = indices.length;
        indicesBuffer = BufferUtils.createByteBuffer(indicesLength);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        // Create a vertex buffer object
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        {

            // Create a VBO for the positions
            vbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            {
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, combinedBuffer, GL15.GL_STATIC_DRAW);
                GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, Vertex.STRIDE, 0);
                GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.COLOR_OFFEST);
                GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_OFFSET);
                GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.NORMAL_OFFSET);
            }
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            // Done with the VAO
        }
        GL30.glBindVertexArray(0);

        // Create a VBO for the indices
        ivbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ivbo);
        {
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        }
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Set the vertices of this rectangle.
     * 
     * @param vertices
     *            The vertices of the rectangle in clockwise order
     */
    public void setVertices(Vertex... vertices)
    {
        combinedBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.STRIDE);

        for (Vertex v : vertices)
        {
            combinedBuffer.put(v.getElements(normal));
        }
        
        combinedBuffer.flip();
    }

    public void update()
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        ByteBuffer vertexByteBuffer = BufferUtils.createByteBuffer(Vertex.STRIDE);
        for (int i = 0; i < vertices.length; i++)
        {
            FloatBuffer vertexFloatBuffer = vertexByteBuffer.asFloatBuffer();
            vertexFloatBuffer.rewind();
            vertexFloatBuffer.put(vertices[i].getElements(normal));
            vertexFloatBuffer.flip();
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, i * Vertex.STRIDE, vertexByteBuffer);
        }
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void update(Vector3f normal, Vertex... vertices)
    {   
        if(this.vertices.length != vertices.length)
        {
            Logging.severe(new IllegalArgumentException("The number of vertices originally in this object does not match the amount given when updated"));
            EngineManager.errorStop(ErrorCodes.FACE_VERTICES_SIZE_MISTMATCH);
        }
        this.normal = normal;
        this.vertices = vertices;
        update();
    }

    public void render()
    {
        if(texture == null)
        {
            GL20.glUseProgram(GLUtil.program);
        }
        else
        {
            GL20.glUseProgram(GLUtil.textureProgram);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        }
        {
            // Bind the texture
            GL30.glBindVertexArray(vao);
            {
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(1);
                GL20.glEnableVertexAttribArray(2);
                GL20.glEnableVertexAttribArray(3);

                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ivbo);
                {
                    // Draw
                    GL11.glDrawElements(GL11.GL_TRIANGLES, indicesLength, GL11.GL_UNSIGNED_BYTE, 0);

                }
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

                GL20.glDisableVertexAttribArray(0);
                GL20.glDisableVertexAttribArray(1);
            }
            GL30.glBindVertexArray(0);
        }
        GL20.glUseProgram(0);
    }

    /**
     * Generate a sequence of indices that will tesselate a polygon with a given number of vertices
     * 
     * @param vertices
     *            The number of vertices on the polygon that this sequence will tesselate
     * @return A sequence of indices that will divide any polygon into a number of triangles (FI: 0, 1, 2, 0, 2, 3 will
     *         split a square into two triangles)
     */
    public static byte[] indexSequence(int vertices)
    {
        if(vertices > MAX_SIDES)
        {
            Logging.severe(new IllegalArgumentException("Size of vertices too large: " + vertices + ". Must not exceed " + MAX_SIDES));
            EngineManager.errorStop(ErrorCodes.POLYGON_SIDES_TOO_LARGE);
        }
        
        byte[] sequence = new byte[vertices * 3];

        for (int i = 0; i < vertices; i++)
        {
            sequence[3 * i + 0] = 0;
            sequence[3 * i + 1] = (byte)(i + 1);
            sequence[3 * i + 2] = (byte)(i + 2);
        }

        return sequence;
    }
}
