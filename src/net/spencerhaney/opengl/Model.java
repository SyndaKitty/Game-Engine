package net.spencerhaney.opengl;

public class Model
{
    private Face[] faces;
    private String name;
    private Matrix4f modelMatrix;
    
    public void init(String name, Face[] faces)
    {
    	this.name = name;
        this.faces = faces;
        modelMatrix = new Matrix4f();
    }
    
    public void render()
    {
    	for(Face face : faces)
    	{
    		//TODO load model matrix
    		face.render();
    	}
    }
    
    public String getModelName()
    {
    	return name;
    }
    
    public int verticesCount()
    {
        int sum = 0;
        for(Face face : faces)
        {
            sum += face.verticesCount();
        }
        return sum;
    }
    
    public int faceCount()
    {
        return faces.length;
    }
}