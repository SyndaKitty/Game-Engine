package net.spencerhaney.engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Resources
{
    public static final String RESOURCE_FOLDER = "res";

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
}