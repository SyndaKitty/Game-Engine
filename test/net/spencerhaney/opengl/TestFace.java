package net.spencerhaney.opengl;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class TestFace
{
    @Test
    public void testIndexSequence()
    {
        byte[] sequence = Face.indexSequence(1);
        System.out.println(Arrays.toString(sequence));
        Assert.assertArrayEquals(sequence, new byte[]{0, 1, 2});
        
        sequence = Face.indexSequence(5);

        System.out.println(Arrays.toString(sequence));
        Assert.assertArrayEquals(sequence, new byte[]{0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 5, 0, 5, 6});
    }
}