package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.util.ObjectsHelper;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

public final class Sphere implements Geometry {

    private final float x, y, z, radius;
    private final Cuboid mbc;

    protected Sphere(float x, float y, float z, float radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.mbc = Cuboid.create(x - radius, y - radius, z - radius,  x + radius, y + radius, z + radius);
    }

    public static Sphere create(double x, double y, double z, double radius) {
        return new Sphere((float) x, (float) y, (float) z, (float) radius);
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }
    
    public float z() {
    	return z;
    }

    @Override
    public Cuboid mbc() {
        return mbc;
    }

    @Override
    public double distance(Cuboid r) {
        return Math.max(0, new Point(x, y, z).distance(r) - radius);
    }

    @Override
    public boolean intersects(Cuboid r) {
        return distance(r) == 0;
    }

    public boolean intersects(Sphere c) {
        double total = radius + c.radius;
        return new Point(x, y, z).distanceSquared(new Point(c.x, c.y, c.z)) <= total * total;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y, radius);
    }

	@Override
	public boolean equals(Object obj) {
		Optional<Sphere> other = ObjectsHelper.asClass(obj, Sphere.class);
		if (other.isPresent()) {
			return Objects.equal(x, other.get().x)
					&& Objects.equal(y, other.get().y)
					&& Objects.equal(z, other.get().z)
					&& Objects.equal(radius, other.get().radius);
		} else
			return false;
	}

	//XXX feels abit redundant
    public boolean intersects(Point point) {
        return Math.sqrt(sqr(x - point.x()) + sqr(y - point.y()) + sqr(z - point.z())) <= radius;
    }

    private float sqr(float x) {
        return x * x;
    }
}
