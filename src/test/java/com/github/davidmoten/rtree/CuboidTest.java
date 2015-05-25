package com.github.davidmoten.rtree;

import static com.github.davidmoten.rtree.geometry.Geometries.cuboid;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.davidmoten.rtree.geometry.Cuboid;

public class CuboidTest {

    private static final double PRECISION = 0.00001;

    @Test
    public void testDistanceToSelfIsZero() {
        Cuboid r = cuboid(0, 0, 0, 1, 1, 0);
        assertEquals(0, r.distance(r), PRECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXParametersWrongOrderThrowsException() {
        cuboid(2, 0, 0, 1, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYParametersWrongOrderThrowsException() {
        cuboid(0, 2, 0, 1, 1, 0);
    }

    @Test
    public void testDistanceToOverlapIsZero() {
        Cuboid r = cuboid(0, 0, 0, 2, 2, 0);
        Cuboid r2 = cuboid(1, 1, 0, 3, 3, 0);

        assertEquals(0, r.distance(r2), PRECISION);
        assertEquals(0, r2.distance(r), PRECISION);
    }

    @Test
    public void testDistanceWhenSeparatedByXOnly() {
        Cuboid r = cuboid(0, 0, 0, 2, 2, 0);
        Cuboid r2 = cuboid(3, 0, 0, 4, 2, 0);

        assertEquals(1, r.distance(r2), PRECISION);
        assertEquals(1, r2.distance(r), PRECISION);
    }

    @Test
    public void testDistanceWhenSeparatedByXOnlyAndOverlapOnY() {
        Cuboid r = cuboid(0, 0, 0, 2, 2, 0);
        Cuboid r2 = cuboid(3, 1.5f, 0, 4, 3.5f, 0);

        assertEquals(1, r.distance(r2), PRECISION);
        assertEquals(1, r2.distance(r), PRECISION);
    }

    @Test
    public void testDistanceWhenSeparatedByDiagonally() {
        Cuboid r = cuboid(0, 0, 0, 2, 1, 0);
        Cuboid r2 = cuboid(3, 6, 0, 10, 8, 0);

        assertEquals(Math.sqrt(26), r.distance(r2), PRECISION);
        assertEquals(Math.sqrt(26), r2.distance(r), PRECISION);
    }

    @Test
    public void testInequalityWithNull() {
        assertFalse(cuboid(0, 0, 0, 1, 1, 0).equals(null));
    }

    @Test
    public void testSimpleEquality() {
        Cuboid r = cuboid(0, 0, 0, 2, 1, 0);
        Cuboid r2 = cuboid(0, 0, 0, 2, 1, 0);

        assertTrue(r.equals(r2));
    }

    @Test
    public void testSimpleInEquality1() {
        Cuboid r = cuboid(0, 0, 0, 2, 1, 0);
        Cuboid r2 = cuboid(0, 0, 0, 2, 2, 0);

        assertFalse(r.equals(r2));
    }

    @Test
    public void testSimpleInEquality2() {
        Cuboid r = cuboid(0, 0, 0, 2, 1, 0);
        Cuboid r2 = cuboid(1, 0, 0, 2, 1, 0);

        assertFalse(r.equals(r2));
    }

    @Test
    public void testSimpleInEquality3() {
        Cuboid r = cuboid(0, 0, 0, 2, 1, 0);
        Cuboid r2 = cuboid(0, 1, 0, 2, 1, 0);

        assertFalse(r.equals(r2));
    }

    @Test
    public void testSimpleInEquality4() {
        Cuboid r = cuboid(0, 0, 0, 2, 2, 0);
        Cuboid r2 = cuboid(0, 0, 0, 1, 2, 0);

        assertFalse(r.equals(r2));
    }

    @Test
    public void testGeometry() {
        Cuboid r = cuboid(0, 0, 0, 2, 1, 0);
        assertTrue(r.equals(r.geometry()));
    }

    @Test
    public void testIntersects() {
        Cuboid a = cuboid(14, 14, 0, 86, 37, 0);
        Cuboid b = cuboid(13, 23, 0, 50, 80, 0);
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    public void testIntersectsNoCuboidContainsCornerOfAnother() {
        Cuboid a = cuboid(10, 10, 0, 50, 50,0);
        Cuboid b = cuboid(28.0, 4.0, 0, 34.0, 85.0, 0);
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    public void testIntersectsOneCuboidContainsTheOther() {
        Cuboid a = cuboid(10, 10,0, 50, 50,0);
        Cuboid b = cuboid(20, 20,0, 40, 40,0);
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }
    
    @Test
    public void testContains() {
        Cuboid r = cuboid(10,20,0,30,40,0);
        assertTrue(r.contains(20,30));
    }
    
    @Test
    public void testContainsReturnsFalseWhenLessThanMinY() {
        Cuboid r = cuboid(10,20,0,30,40,0);
        assertFalse(r.contains(20,19));
    }
    
    @Test
    public void testContainsReturnsFalseWhenGreaterThanMaxY() {
        Cuboid r = cuboid(10,20,0,30,40,0);
        assertFalse(r.contains(20,41));
    }
    
    @Test
    public void testContainsReturnsFalseWhenGreaterThanMaxX() {
        Cuboid r = cuboid(10,20,0,30,40,0);
        assertFalse(r.contains(31,30));
    }
    
    @Test
    public void testContainsReturnsFalseWhenLessThanMinX() {
        Cuboid r = cuboid(10,20,0,30,40,0);
        assertFalse(r.contains(9,30));
    }

}