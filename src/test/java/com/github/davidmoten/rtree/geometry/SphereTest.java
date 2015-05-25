package com.github.davidmoten.rtree.geometry;

import static com.github.davidmoten.rtree.geometry.Geometries.sphere;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SphereTest {
    private static final double PRECISION = 0.000001;

    @Test
    public void testCoordinates() {
        Sphere sphere = sphere(1, 2, 0, 3);
        assertEquals(1, sphere.x(), PRECISION);
        assertEquals(2, sphere.y(), PRECISION);
    }

    @Test
    public void testDistance() {
        Sphere sphere = sphere(0, 0, 0, 1);
        Cuboid r = Geometries.cuboid(1, 1, 0, 2, 2, 0);
        assertEquals(Math.sqrt(2) - 1, sphere.distance(r), PRECISION);
    }

    @Test
    public void testMbr() {
        Sphere sphere = sphere(1, 2, 0, 3);
        Cuboid r = Geometries.cuboid(-2, -1, -3, 4, 5, 3);
        assertEquals(r, sphere.mbc());
    }

    @Test
    public void testEquality() {
        Sphere sphere1 = sphere(1, 2, 0, 3);
        Sphere sphere2 = sphere(1, 2, 0, 3);
        assertEquals(sphere1, sphere2);
    }

    @Test
    public void testInequalityRadius() {
        Sphere sphere1 = sphere(1, 2, 0, 3);
        Sphere sphere2 = sphere(1, 2, 0, 4);
        assertNotEquals(sphere1, sphere2);
    }

    @Test
    public void testInequalityX() {
        Sphere sphere1 = sphere(1, 2, 0, 3);
        Sphere sphere2 = sphere(2, 2, 0, 3);
        assertNotEquals(sphere1, sphere2);
    }

    @Test
    public void testInequalityY() {
        Sphere sphere1 = sphere(1, 2, 0, 3);
        Sphere sphere2 = sphere(1, 3, 0, 3);
        assertNotEquals(sphere1, sphere2);
    }

    @Test
    public void testInequalityWithNull() {
        Sphere sphere = sphere(1, 2, 0, 3);
        assertFalse(sphere.equals(null));
    }

    @Test
    public void testHashCode() {
        Sphere sphere = sphere(1, 2, 0, 3);
        assertEquals(1606448223, sphere.hashCode());
    }

    @Test
    public void testDistanceIsZeroWhenIntersects() {
        Sphere sphere = sphere(0, 0, 0, 1);
        assertTrue(sphere.distance(Geometries.cuboid(0, 1, 0, 0, 1, 0)) == 0);
    }

    @Test
    public void testIntersects2() {
        Sphere sphere = sphere(0, 0, 0, 1);
        assertTrue(sphere.distance(Geometries.cuboid(0, 1.1, 0, 0, 1.1, 0)) != 0);
    }

    @Test
    public void testIntersects3() {
        Sphere sphere = sphere(0, 0, 0, 1);
        assertTrue(sphere.distance(Geometries.cuboid(1, 1, 0, 1, 1, 0)) != 0);
    }

    @Test
    public void testIntersectsReturnsTrue() {
        assertTrue(sphere(0, 0, 0, 1).intersects(Geometries.cuboid(0, 0, 0, 1, 1, 0)));
    }

    @Test
    public void testIntersectsReturnsFalse() {
        assertFalse(sphere(0, 0, 0, 1).intersects(Geometries.cuboid(10, 10, 0, 11, 11, 0)));
    }
    
    @Test
    public void testIntersects() {
        Sphere a = sphere(0,0,0,1);
        Sphere b = sphere(0.1,0.1,0, 1);
        assertTrue(a.intersects(b));
    }
    
    @Test
    public void testDoNotIntersect() {
        Sphere a = sphere(0,0,0,1);
        Sphere b = sphere(100,100,0, 1);
        assertFalse(a.intersects(b));
    }
    
    @Test
    public void testIntersectsPoint() {
        assertTrue(sphere(0,0,0,1).intersects(Geometries.point(0,0,0)));
    }
    
    @Test
    public void testDoesNotIntersectPoint() {
        assertFalse(sphere(0,0,0,1).intersects(Geometries.point(100,100,0)));
    }
}
