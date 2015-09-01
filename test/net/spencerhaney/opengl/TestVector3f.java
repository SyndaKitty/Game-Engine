package net.spencerhaney.opengl;

import org.junit.Assert;
import org.junit.Test;

public class TestVector3f
{
    @Test
    public void testThatConstantsAreNormalized()
    {
        double sum = Vector3f.UP.getX() * Vector3f.UP.getX() + Vector3f.UP.getY() * Vector3f.UP.getY()
                + Vector3f.UP.getZ() * Vector3f.UP.getZ();
        Assert.assertEquals(1.0, Math.sqrt(sum), .001);

        sum = Vector3f.DOWN.getX() * Vector3f.DOWN.getX() + Vector3f.DOWN.getY() * Vector3f.DOWN.getY()
                + Vector3f.DOWN.getZ() * Vector3f.DOWN.getZ();
        Assert.assertEquals(1.0, Math.sqrt(sum), .001);

        sum = Vector3f.LEFT.getX() * Vector3f.LEFT.getX() + Vector3f.LEFT.getY() * Vector3f.LEFT.getY()
                + Vector3f.LEFT.getZ() * Vector3f.LEFT.getZ();
        Assert.assertEquals(1.0, Math.sqrt(sum), .001);

        sum = Vector3f.RIGHT.getX() * Vector3f.RIGHT.getX() + Vector3f.RIGHT.getY() * Vector3f.RIGHT.getY()
                + Vector3f.RIGHT.getZ() * Vector3f.RIGHT.getZ();
        Assert.assertEquals(1.0, Math.sqrt(sum), .001);

        sum = Vector3f.TOWARD.getX() * Vector3f.TOWARD.getX() + Vector3f.TOWARD.getY() * Vector3f.TOWARD.getY()
                + Vector3f.TOWARD.getZ() * Vector3f.TOWARD.getZ();
        Assert.assertEquals(1.0, Math.sqrt(sum), .001);

        sum = Vector3f.AWAY.getX() * Vector3f.AWAY.getX() + Vector3f.AWAY.getY() * Vector3f.AWAY.getY()
                + Vector3f.AWAY.getZ() * Vector3f.AWAY.getZ();
        Assert.assertEquals(1.0, Math.sqrt(sum), .001);
    }

    @Test
    public void testThatDefaultConstructorHasZeroes()
    {
        Vector3f v = new Vector3f();
        Assert.assertEquals(v.getX(), 0, 0);
        Assert.assertEquals(v.getY(), 0, 0);
        Assert.assertEquals(v.getZ(), 0, 0);
    }

    @Test
    public void testThatSetXYZThrowsException()
    {
        Vector3f v = new Vector3f();
        try
        {
            v.setXYZ(new float[] {1, 2, 3, 4});
            Assert.fail("SetXYZ() did not throw exception when given illegal arguments");
        }
        catch (IllegalArgumentException e)
        {

        }

        try
        {
            v.setXYZ(new float[] {1, 2});
            Assert.fail("SetXYZ() did not throw exception when given illegal arguments");
        }
        catch (IllegalArgumentException e)
        {

        }

        try
        {
            v.setXYZ(new float[] {1, 2, 3});
        }
        catch (Exception e)
        {
            Assert.fail("SetXYZ() failed when given correct arguments");
        }
    }
    
    @Test
    public void testThatToStringGivesNumbers()
    {
        Vector3f v = new Vector3f(1.5f, 2.5f, 3.5f);
        String s = v.toString();
        boolean hasInfo = s.contains(Float.toString(1.5f)) && s.contains(Float.toString(2.5f))
                && s.contains(Float.toString(3.5f));
        Assert.assertTrue("ToString() did not give enough information back", hasInfo);
    }
}
