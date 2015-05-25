package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.util.ObjectsHelper;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public final class Cuboid implements Geometry, HasGeometry {
    private final float x1, y1,z1, x2, y2, z2;

    protected Cuboid(float x1, float y1, float z1, float x2, float y2, float z2) {
        Preconditions.checkArgument(x2 >= x1);
        Preconditions.checkArgument(y2 >= y1);
        Preconditions.checkArgument(z2 >= z1);
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public float x1() {
        return x1;
    }

    public float y1() {
        return y1;
    }
    
    public float z1() {
    	return z1;
    }

    public float x2() {
        return x2;
    }

    public float y2() {
        return y2;
    }
    
    public float z2()  {
    	return z2;
    }

    public float volume() {
        return (x2 - x1) * (y2 - y1) * (z2 - z1);
    }

    public Cuboid add(Cuboid r) {
        return new Cuboid(Math.min(x1, r.x1), Math.min(y1, r.y1), Math.min(z1, r.z1), Math.max(x2, r.x2), Math.max(
                y2, r.y2), Math.max(z2, r.z2));
    }

    public static Cuboid create(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new Cuboid((float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
    }

    public static Cuboid create(float x1, float y1, float z1, float x2, float y2, float z2) {
        return new Cuboid(x1, y1, z1, x2, y2, z2);
    }

    public boolean contains(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    @Override
	/**
	 * No overlap if
	 * Cond1.  If A's left face is to the right of the B's right face,
	 *     -  then A is Totally to right Of B
	 *        CubeA.X1 > CubeB.X2
	 * Cond2.  If A's right face is to the left of the B's left face,
	 *     -  then A is Totally to left Of B
	 *        CubeA.X2 < CubeB.X1  
	 * Cond3.  If A's top face is below B's bottom face,
	 *      -  then A is Totally below B
	 *         CubeA.Z2 < CubeB.Z1
	 * Cond4.  If A's bottom face is above B's top face,
	 *      -  then A is Totally above B
	 *         CubeA.Z1 > CubeB.Z2  
	 * Cond5.  If A's front face is behind B's back face,
	 *      -  then A is Totally behind B
	 *         CubeA.Y1 > CubeB.Y2
	 * Cond6.  If A's back face is in front of B's front face,
	 *      -  then A is Totally in front of B
	 *         CubeA.Y2 < CubeB.Y1
	 *         
	 *         Not Cond1 AND Not Cond2 And Not Cond3 And Not Cond4 And Not Cond5 And Not Cond6
	 */
    public boolean intersects(Cuboid r) {
    	return !(x1 > r.x2)
    		&& !(x2 < r.x1)
    		&& !(z1 > r.z2) 
    		&& !(z2 < r.z1)
    		&& !(y1 > r.y2)
    		&& !(y2 < r.y1);
    }

    @Override
    public double distance(Cuboid r) {
        if (intersects(r))
            return 0;
        else {
            Cuboid mostLeft = x1 < r.x1 ? this : r;
            Cuboid mostRight = x1 > r.x1 ? this : r;
            
            double xDifference = Math.max(0, mostLeft.x1 == mostRight.x1 ? 0 : mostRight.x1 - mostLeft.x2);

            Cuboid lower = y1 < r.y1 ? this : r;
            Cuboid upper = y1 > r.y1 ? this : r;

            double yDifference = Math.max(0, upper.y1 == lower.y1 ? 0 : upper.y1 - lower.y2);

            Cuboid below = z1 < r.z1 ? this : r;
            Cuboid above = z1 > r.z1 ? this : r;

            double zDifference = Math.max(0, above.z1 == below.z1 ? 0 : above.z1 - below.z2);

            return Math.sqrt(xDifference * xDifference + yDifference * yDifference + zDifference * zDifference);
        }
    }

    @Override
    public Cuboid mbc() {
        return this;
    }

    @Override
    public String toString() {
        return "Cuboid [x1=" + x1 + ", y1=" + y1 + "z1=" + z1 + ", x2=" + x2 + ", y2=" + y2 + ", z2=" + z2 + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public boolean equals(Object obj) {
        Optional<Cuboid> other = ObjectsHelper.asClass(obj, Cuboid.class);
        if (other.isPresent()) {
            return Objects.equal(x1, other.get().x1) && Objects.equal(x2, other.get().x2)
                    && Objects.equal(y1, other.get().y1) && Objects.equal(y2, other.get().y2)
                    && Objects.equal(z1, other.get().z1) && Objects.equal(z2, other.get().z2);
        } else
            return false;
    }

    public float intersectionVolume(Cuboid r) {
        if (!intersects(r))
            return 0;
        else
            return create(Math.max(x1, r.x1), Math.max(y1, r.y1), Math.max(z1, r.z1), Math.min(x2, r.x2),
                    Math.min(y2, r.y2), Math.min(z2, r.z2)).volume();
    }

    public float surfaceArea() {
        return 2 * (x2 - x1) * (y2 - y1) + 2 * (y2 - y1)*(z2 - z1) + 2 * (z2 - z1)*(x2 - x1);
    }

    @Override
    public Geometry geometry() {
        return this;
    }

}
