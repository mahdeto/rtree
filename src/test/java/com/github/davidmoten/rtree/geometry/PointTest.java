package com.github.davidmoten.rtree.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PointTest {

    private static final double PRECISION = 0.000001;

    @Test
    public void testCoordinates() {
        Point point = Geometries.point(1, 2, 0);
        assertEquals(1, point.x(), PRECISION);
        assertEquals(2, point.y(), PRECISION);
    }

    @Test
    public void testDistanceToCuboid() {
        Point p1 = Geometries.point(1, 2, 0);
        Cuboid r = Geometries.cuboid(4, 6, 0, 4, 6, 0);
        assertEquals(5, p1.distance(r), PRECISION);
    }

    @Test
    public void testDistanceToPoint() {
        Point p1 = Geometries.point(1, 2, 0);
        Point p2 = Geometries.point(4, 6, 0);
        assertEquals(5, p1.distance(p2), PRECISION);
    }

    @Test
    public void testMbr() {
        Point p = Geometries.point(1, 2, 0);
        Cuboid r = Geometries.cuboid(1, 2, 0, 1, 2, 0);
        assertEquals(r, p.mbc());
    }

    @Test
    public void testPointIntersectsItself() {
        Point p = Geometries.point(1, 2, 0);
        assertTrue(p.distance(p.mbc()) == 0);
    }

    @Test
    public void testIntersectIsFalseWhenPointsDiffer() {
        Point p1 = Geometries.point(1, 2, 0);
        Point p2 = Geometries.point(1, 2.000001, 0);
        assertFalse(p1.distance(p2.mbc()) == 0);
    }

    @Test
    public void testEquality() {
        Point p1 = Geometries.point(1, 2, 0);
        Point p2 = Geometries.point(1, 2, 0);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testInequality() {
        Point p1 = Geometries.point(1, 2, 0);
        Point p2 = Geometries.point(1, 3, 0);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testInequalityToNull() {
        Point p1 = Geometries.point(1, 2, 0);
        assertFalse(p1.equals(null));
    }

    @Test
    public void testHashCode() {
        Point p = Geometries.point(1, 2, 0);
        assertEquals(82197344, p.hashCode());
    }
}
