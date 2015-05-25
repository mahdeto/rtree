package com.github.davidmoten.rtree.geometry;

public final class Geometries {

    private Geometries() {
        // prevent instantiation
    }

    public static Point point(double x, double y, double z) {
        return Point.create(x, y, z);
    }

    public static Cuboid cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Cuboid.create(x1, y1, z1, x2, y2, z2);
    }

    public static Sphere sphere(double x, double y, double z, double radius) {
        return Sphere.create(x, y, z, radius);
    }
    
}
