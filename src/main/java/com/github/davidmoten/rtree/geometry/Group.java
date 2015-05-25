package com.github.davidmoten.rtree.geometry;

import java.util.List;

import com.github.davidmoten.rtree.Util;

public class Group<T extends HasGeometry> implements HasGeometry {

    private final List<T> list;
    private final Cuboid mbc;

    public Group(List<T> list) {
        this.list = list;
        this.mbc = Util.mbc(list);
    }

    public List<T> list() {
        return list;
    }

    @Override
    public Geometry geometry() {
        return mbc;
    }

}
