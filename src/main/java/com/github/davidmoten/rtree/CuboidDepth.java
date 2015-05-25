package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Cuboid;

final class CuboidDepth {
    private final Cuboid cuboid;
    private final int depth;

    CuboidDepth(Cuboid cuboid, int depth) {
        this.cuboid= cuboid;;
        this.depth = depth;
    }

    Cuboid getCuboid() {
        return cuboid;
    }

    int getDepth() {
        return depth;
    }

}
