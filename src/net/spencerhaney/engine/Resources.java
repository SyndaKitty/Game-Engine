package net.spencerhaney.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Resources
{
    public static final String RESOURCE_FOLDER = "res";

    //TODO map resources to a File key
    private static Map<String, Object[]> resources;

    /**
     * Loads all of the resources in the res folder that can be recognized.
     */
    public static void init()
    {
        resources = new HashMap<String, Object[]>();
        Path resourceFolder = Paths.get(RESOURCE_FOLDER);
        try
        {
            loadDirectory(resourceFolder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        Logging.info("Resources loaded successfully");
    }

    private static void loadDirectory(Path directory) throws IOException
    {
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);
        for (Path path : directoryStream)
        {
            File file = path.toFile();
            if (file.exists())
            {
                if (file.isDirectory())
                {
                    loadDirectory(path);
                }
                else if (file.isFile())
                {
                    loadFile(path.toString());
                }
            }
        }
    }

    private static void loadFile(String s) throws IOException
    {
        Logging.fine("Attempting to load " + s);
        if (s.toLowerCase().endsWith("png"))
        {
            resources.put(s, loadPNG(s));
        }
        else
        {
            Logging.fine("Could not read \"" + s + "\"");
        }
    }

    /**
     * Load and decode a PNG file. Store in an Object array in the order, {ByteBuffer, width, height}
     * @param filePath The file path of the texture to laod
     * @return Object array[] {Bytebuffer bytes, int width, int height}
     * @throws IOException 
     */
    public static Object[] loadPNG(final String filePath) throws IOException
    {
        InputStream in = new FileInputStream(filePath);
        PNGDecoder decoder = new PNGDecoder(in);

        Logging.fine("Loading \"" + filePath + "\" - " + decoder.getWidth() + "x" + decoder.getHeight() + "px");

        final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
        buffer.flip();
        return new Object[]{buffer, decoder.getWidth(), decoder.getHeight()};
    }
    
    public static Object[] getResource(final String filePath)
    {
        return resources.get(filePath);
    }
}
