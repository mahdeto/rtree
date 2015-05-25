package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.util.ObjectsHelper;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

public final class Point implements Geometry {

    private final Cuboid mbc;

    protected Point(float x, float y, float z) {
        this.mbc = Cuboid.create(x, y, z, x, y, z);
    }

    public static Point create(double x, double y, double z) {
        return new Point((float) x, (float) y, (float) z);
    }

    @Override
    public Cuboid mbc() {
        return mbc;
    }

    @Override
    public double distance(Cuboid r) {
        return mbc.distance(r);
    }

    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    public double distanceSquared(Point p) {
        float dx = mbc().x1() - p.mbc().x1();
        float dy = mbc().y1() - p.mbc().y1();
        float dz = mbc().z1() - p.mbc().z1();
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public boolean intersects(Cuboid r) {
        return mbc.intersects(r);
    }

    public float x() {
        return mbc.x1();
    }

    public float y() {
        return mbc.y1();
    }
    
    public float z() {
    	return mbc.z1();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mbc);
    }

    @Override
    public boolean equals(Object obj) {
        Optional<Point> other = ObjectsHelper.asClass(obj, Point.class);
        if (other.isPresent()) {
            return Objects.equal(mbc, other.get().mbc());
        } else
            return false;
    }

    @Override
    public String toString() {
        return "Point [x=" + x() + ", y=" + y() + ", z=" + z() + "]";
    }

}