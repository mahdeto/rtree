package com.github.davidmoten.rtree;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.davidmoten.rtree.geometry.Cuboid;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

/**
 * @author dxm
 *
 */
public final class Util {

    private Util() {
        // prevent instantiation
    }

    /**
     * Returns the minimum bounding rectangle of a number of items. Benchmarks
     * below indicate that when the number of items is &gt;1 this method is more
     * performant than one using {@link Rectangle#add(Rectangle)}.
     * 
     * <pre>
     * Benchmark                             Mode  Samples         Score  Score error  Units
     * c.g.d.r.BenchmarksMbr.mbrList1       thrpt       10  48450492.301   436127.960  ops/s
     * c.g.d.r.BenchmarksMbr.mbrList2       thrpt       10  46658242.728   987901.581  ops/s
     * c.g.d.r.BenchmarksMbr.mbrList3       thrpt       10  40357809.306   937827.660  ops/s
     * c.g.d.r.BenchmarksMbr.mbrList4       thrpt       10  35930532.557   605535.237  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList1    thrpt       10  55848118.198  1342997.309  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList2    thrpt       10  25171873.903   395127.918  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList3    thrpt       10  19222116.139   246965.178  ops/s
     * c.g.d.r.BenchmarksMbr.mbrOldList4    thrpt       10  14891862.638   198765.157  ops/s
     * </pre>
     * 
     * @param items
     *            items to bound
     * @return the minimum bounding rectangle containings items
     */
    public static Cuboid mbc(Collection<? extends HasGeometry> items) {
        Preconditions.checkArgument(!items.isEmpty());
        Cuboid some = Iterables.get(items, 0).geometry().mbc();
        float minX1 = some.x1();
        float minY1 = some.y1();
        float minZ1 = some.z1();
        float maxX2 = some.x2();
        float maxY2 = some.y2();
        float maxZ2 = some.z2();
        
        for (final HasGeometry item : items) {
            Cuboid r = item.geometry().mbc();
            if (r.x1() < minX1)
                minX1 = r.x1();
            if (r.y1() < minY1)
                minY1 = r.y1();
            if (r.z1() < minZ1)
            	minZ1 = r.z1();
            if (r.x2() > maxX2)
                maxX2 = r.x2();
            if (r.y2() > maxY2)
                maxY2 = r.y2();
            if (r.z2() > maxZ2) 
            	maxZ2 = r.z2();
        }
        return Cuboid.create(minX1, minY1, minZ1, maxX2, maxY2, maxZ2);
    }

    static <T> List<T> add(List<T> list, T element) {
        final ArrayList<T> result = new ArrayList<T>(list.size() + 2);
        result.addAll(list);
        result.add(element);
        return result;
    }

    static <T> List<T> remove(List<? extends T> list, List<? extends T> elements) {
        final ArrayList<T> result = new ArrayList<T>(list);
        result.removeAll(elements);
        return result;
    }

    static <T> List<? extends T> replace(List<? extends T> list, T element, List<T> replacements) {
        List<T> list2 = new ArrayList<T>(list.size() + replacements.size());
        for (T node : list)
            if (node != element)
                list2.add(node);
        list2.addAll(replacements);
        return list2;
    }
}
