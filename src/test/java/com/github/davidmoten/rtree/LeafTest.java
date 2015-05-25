package com.github.davidmoten.rtree;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Cuboid;

public class LeafTest {

    private static Context context = new Context(2, 4, new SelectorMinimalAreaIncrease(),
            new SplitterQuadratic());

    @Test(expected = IllegalArgumentException.class)
    public void testCannotHaveZeroChildren() {
        new Leaf<Object, Cuboid>(new ArrayList<Entry<Object, Cuboid>>(), context);
    }

    @Test
    public void testMbr() {
        Cuboid r1 = Geometries.cuboid(0, 1, 0, 3, 5, 0);
        Cuboid r2 = Geometries.cuboid(1, 2, 0, 4, 6, 0);
        @SuppressWarnings("unchecked")
        Cuboid r = new Leaf<Object, Cuboid>(Arrays.asList(Entry.entry(new Object(), r1),
                Entry.entry(new Object(), r2)), context).geometry().mbc();
        assertEquals(r1.add(r2), r);
    }
}
