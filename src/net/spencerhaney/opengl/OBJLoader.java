package net.spencerhaney.opengl;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import net.spencerhaney.engine.Logging;

public class OBJLoader
{
    private static final String COMMENT = "#";
    private static final String OBJECT = "o";
    private static final String NORMAL = "vn";
    private static final String VERTEX = "v";
    private static final String FACE = "f";
    private static final String FACE_SPLITTER = "/";
    private static final String SPACE = " ";
    private static final String EMPTY = "";
    private static final String DEFAULT_MODEL_NAME = "defaultModel";

    public static Model load(Path p)
    {
        Logging.fine("Loading " + p);
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Face> faces = new ArrayList<Face>();
        ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
        Model model = new Model();
        String name = DEFAULT_MODEL_NAME;

        try (Scanner in = new Scanner(p.toFile()))
        {
            String line;
            while (in.hasNextLine())
            {
                line = in.nextLine().trim();

                // Remove comments
                int commentIndex = line.indexOf(COMMENT);
                if (commentIndex >= 0)
                {
                    line = line.substring(0, commentIndex).trim();
                    if (line.trim().equals(EMPTY))
                    {
                        continue;
                    }
                }

                // Now the lines are cleaned, parse them
                if (line.startsWith(OBJECT))
                {
                    name = line.split(OBJECT)[1];
                }
                else if (line.startsWith(NORMAL))
                {
                    String[] splits = line.split(SPACE);
                    float x = Float.parseFloat(splits[1]);
                    float y = Float.parseFloat(splits[2]);
                    float z = Float.parseFloat(splits[3]);
                    normals.add(new Vector3f(x, y, z));
                }
                else if (line.startsWith(VERTEX))
                {
                    float scale = 0.5f;
                    String[] splits = line.split(SPACE);
                    float x = Float.parseFloat(splits[1]) * scale;
                    float y = Float.parseFloat(splits[2]) * scale;
                    float z = Float.parseFloat(splits[3]) * scale;
                    vertices.add(new Vertex(x, y, z));
                }
                else if (line.startsWith(FACE))
                {
                    ArrayList<Vector3f> localNormals = new ArrayList<Vector3f>();
                    ArrayList<Vertex> localVertices = new ArrayList<Vertex>();

                    String[] splits = line.split(SPACE);
                    for (int i = 1; i < splits.length; i++)
                    {
                        String[] indices = splits[i].split(FACE_SPLITTER);
                        int vertexIndex = Integer.parseInt(indices[0]) - 1;
                        localVertices.add(vertices.get(vertexIndex));
                        // TODO add uv coords
                        // TODO catch cases without normals
                        localNormals.add(normals.get(Integer.parseInt(indices[2]) - 1));
                    }
                    Vector3f[] normalArray = new Vector3f[localNormals.size()];
                    localNormals.toArray(normalArray);
                    Vertex[] vertexArray = new Vertex[localVertices.size()];
                    localVertices.toArray(vertexArray);
                    Face f = new Face();
                    f.init(normalArray, vertexArray);
                    faces.add(f);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            Logging.severe(e);
        }
        Face[] c = new Face[faces.size()];
        model.init(name, faces.toArray(c));
        Logging.fine("Loaded " + p.toString() + " - " + model.faceCount() + " faces, " + model.verticesCount()
                + " vertices, " + vertices.size() + " unique vertices.");
        return model;
    }
}
