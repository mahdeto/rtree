package com.github.davidmoten.rtree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.davidmoten.rtree.geometry.Cuboid;
import com.github.davidmoten.rtree.geometry.Geometries;

public class Utilities {

    static List<Entry<Object, Cuboid>> entries1000() {
        List<Entry<Object, Cuboid>> list = new ArrayList<Entry<Object, Cuboid>>();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                BenchmarksRTree.class.getResourceAsStream("/1000.txt")));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] items = line.split(" ");
                double x = Double.parseDouble(items[0]);
                double y = Double.parseDouble(items[1]);
                double z = 0;
//                list.add(Entry.entry(new Object(), Geometries.cuboid(x, y, z, x + 1, y + 1, z + 1)));
                list.add(Entry.entry(new Object(), Geometries.cuboid(x, y, z, x + 1, y + 1, z)));
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
