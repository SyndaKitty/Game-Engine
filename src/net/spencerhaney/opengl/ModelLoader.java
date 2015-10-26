package net.spencerhaney.opengl;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.lwjgl.opengl.GL13;

import net.spencerhaney.engine.EngineManager;
import net.spencerhaney.engine.ErrorCodes;
import net.spencerhaney.engine.Logging;

public class ModelLoader
{
    private static Map<String, Object[]> models = new HashMap<>();
    
    public static Model load(Path model, Path texture)
    {
        String key = model.normalize().toString() + "|" + texture.normalize().toString();
        if(models.containsKey(key))
        {
            Object[] objects = models.get(key);
            Model m = new Model();
            
            List<Vector3f> vertices = (List)objects[0];
            List<Vector2f> uvs = (List)objects[1];
            List<Vector3f> normals = (List)objects[2];
            int tex = (int)objects[3];
            
            m.init(vertices, uvs, normals, tex);
            return m;
        }
        
        Logging.fine("Loading " + model);
        List<Integer> vertexIndices = new ArrayList<>();
        List<Integer> uvIndices = new ArrayList<>();
        List<Integer> normalIndices = new ArrayList<>();
        List<Vector3f> tempVertices = new ArrayList<>();
        List<Vector3f> tempNormals = new ArrayList<>();
        List<Vector2f> tempUVs = new ArrayList<>();

        Scanner in = null;
        try
        {
            in = new Scanner(model.toFile());
        }
        catch (FileNotFoundException e)
        {
            EngineManager.errorStop(ErrorCodes.OBJ_FILE_NOT_FOUND);
        }
        String line;
        while (in.hasNextLine())
        {
            line = in.nextLine();
            String[] tokens = line.split(" ");
            String header = tokens[0];

            if (header.equalsIgnoreCase("v"))
            {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                tempVertices.add(new Vector3f(x, y, z));
            }
            else if (header.equalsIgnoreCase("vt"))
            {
                float x = Float.parseFloat(tokens[1]);
                float y = 1 - Float.parseFloat(tokens[2]);
                tempUVs.add(new Vector2f(x, y));
            }
            else if (header.equalsIgnoreCase("vn"))
            {
                float x = Float.parseFloat(tokens[1]);
                float y = Float.parseFloat(tokens[2]);
                float z = Float.parseFloat(tokens[3]);
                tempNormals.add(new Vector3f(x, y, z));
            }
            else if (header.equalsIgnoreCase("f"))
            {
                int[] vertexIndex = new int[3];
                int[] uvIndex = new int[3];
                int[] normalIndex = new int[3];

                for (int i = 0; i < 3; i++)
                {
                    String[] indices = tokens[i + 1].split("/");
                    vertexIndex[i] = Integer.parseInt(indices[0]);
                    uvIndex[i] = Integer.parseInt(indices[1]);
                    normalIndex[i] = Integer.parseInt(indices[2]);
                }

                for (int i = 0; i < 3; i++)
                {
                    vertexIndices.add(vertexIndex[i]);
                    uvIndices.add(uvIndex[i]);
                    normalIndices.add(normalIndex[i]);
                }
            }
        }

        List<Vector3f> outVertices = new ArrayList<>();
        List<Vector2f> outUVs = null;
        outUVs = new ArrayList<>();
        List<Vector3f> outNormals = new ArrayList<>();

        for (int i = 0; i < vertexIndices.size(); i++)
        {
            int vertexIndex = vertexIndices.get(i);
            outVertices.add(tempVertices.get(vertexIndex - 1));

            int uvIndex = uvIndices.get(i);
            outUVs.add(tempUVs.get(uvIndex - 1));

            int normalIndex = normalIndices.get(i);
            outNormals.add(tempNormals.get(normalIndex - 1));
        }
        in.close();

        int tex = GLUtil.createTexture(texture, GL13.GL_TEXTURE0);
        
        Model m = new Model();
        m.init(outVertices, outUVs, outNormals, tex);
        Object[] objects = {outVertices, outUVs, outNormals, tex};
        models.put(key, objects);
        return m;
    }
}
