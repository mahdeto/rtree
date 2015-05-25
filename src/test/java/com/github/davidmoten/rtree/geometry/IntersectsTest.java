package com.github.davidmoten.rtree.geometry;

import static com.github.davidmoten.rtree.geometry.Geometries.sphere;
import static com.github.davidmoten.rtree.geometry.Geometries.cuboid;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.davidmoten.util.TestingUtil;

public class IntersectsTest {

    @Test
    public void testConstructorIsPrivate() {
        TestingUtil.callConstructorAndCheckIsPrivate(Intersects.class);
    }
    
    @Test
    public void testRectangleIntersectsSphere() {
        assertTrue(Intersects.cuboidIntersectsSphere.call(cuboid(0, 0, 0, 0, 0, 0), sphere(0, 0, 0, 1)));
    }
    
    @Test
    public void testRectangleDoesNotIntersectSphere() {
        assertFalse(Intersects.cuboidIntersectsSphere.call(cuboid(0, 0, 0, 0, 0, 0), sphere(100, 100, 0, 1)));
    }
    
}
