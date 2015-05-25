package com.github.davidmoten.rtree.geometry;

import rx.functions.Func2;

public class Intersects {
    
    private Intersects() {
        // prevent instantiation
    }

	public static final Func2<Cuboid, Sphere, Boolean> rectangleIntersectsCircle = new Func2<Cuboid, Sphere, Boolean>() {
		@Override
		public Boolean call(Cuboid rectangle, Sphere circle) {
			return circle.intersects(rectangle);
		}
	};

	public static final Func2<Point, Sphere, Boolean> pointIntersectsCircle = new Func2<Point, Sphere, Boolean>() {
		@Override
		public Boolean call(Point point, Sphere circle) {
			return circle.intersects(point);
		}
	};

}
