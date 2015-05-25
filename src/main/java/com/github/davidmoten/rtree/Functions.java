package com.github.davidmoten.rtree;

import java.util.List;

import rx.functions.Func1;

import com.github.davidmoten.rtree.geometry.Cuboid;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.ListPair;

/**
 * Utility functions for making {@link Selector}s and {@link Splitter}s.
 *
 */
public final class Functions {

    private Functions() {
        // prevent instantiation
    }

    public static final Func1<ListPair<? extends HasGeometry>, Double> overlapListPair = new Func1<ListPair<? extends HasGeometry>, Double>() {

        @Override
        public Double call(ListPair<? extends HasGeometry> pair) {
            return (double) pair.group1().geometry().mbc()
                    .intersectionVolume(pair.group2().geometry().mbc());
        }
    };

    public static Func1<HasGeometry, Double> overlapVolume(final Cuboid r,
            final List<? extends HasGeometry> list) {
        return new Func1<HasGeometry, Double>() {

            @Override
            public Double call(HasGeometry g) {
                Cuboid gPlusR = g.geometry().mbc().add(r);
                double m = 0;
                for (HasGeometry other : list) {
                    if (other != g) {
                        m += gPlusR.intersectionVolume(other.geometry().mbc());
                    }
                }
                return m;
            }
        };
    }

    public static Func1<HasGeometry, Double> volumeIncrease(final Cuboid r) {
        return new Func1<HasGeometry, Double>() {
            @Override
            public Double call(HasGeometry g) {
                Cuboid gPlusR = g.geometry().mbc().add(r);
                return (double) (gPlusR.volume() - g.geometry().mbc().volume());
            }
        };
    }

}
