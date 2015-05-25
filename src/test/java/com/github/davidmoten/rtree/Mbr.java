package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Cuboid;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;

public class Mbr implements HasGeometry {

    private final Cuboid r;

    public Mbr(Cuboid r) {
        this.r = r;
    }

    @Override
    public Geometry geometry() {
        return r;
    }

}
