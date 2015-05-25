package com.github.davidmoten.rtree;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.davidmoten.rtree.geometry.Cuboid;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.google.common.base.Optional;

public final class Visualizer {

    private final RTree<?, Geometry> tree;
    private final int width;
    private final int height;
    private final Cuboid view;
    private final int maxDepth;

    Visualizer(RTree<?, Geometry> tree, int width, int height, Cuboid view) {
        this.tree = tree;
        this.width = width;
        this.height = height;
        this.view = view;
        this.maxDepth = calculateMaxDepth(tree.root());
    }

    private static <R, S extends Geometry> int calculateMaxDepth(Optional<? extends Node<R, S>> root) {
        if (!root.isPresent())
            return 0;
        else
            return calculateDepth(root.get(), 0);
    }

    private static <R, S extends Geometry> int calculateDepth(Node<R, S> node, int depth) {
        if (node instanceof Leaf)
            return depth + 1;
        else
            return calculateDepth(((NonLeaf<R, S>) node).children().get(0), depth + 1);
    }

    public BufferedImage createImage() {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.white);
        g.clearRect(0, 0, width, height);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));

        if (tree.root().isPresent()) {
            final List<CuboidDepth> nodeDepths = getNodeDepthsSortedByDepth(tree.root().get());
            drawNode(g, nodeDepths);
        }
        return image;
    }

    private <T, S extends Geometry> List<CuboidDepth> getNodeDepthsSortedByDepth(Node<T, S> root) {
        final List<CuboidDepth> list = getRectangleDepths(root, 0);
        Collections.sort(list, new Comparator<CuboidDepth>() {

            @Override
            public int compare(CuboidDepth n1, CuboidDepth n2) {
                return ((Integer) n1.getDepth()).compareTo(n2.getDepth());
            }
        });
        return list;
    }

    private <T, S extends Geometry> List<CuboidDepth> getRectangleDepths(Node<T, S> node,
            int depth) {
        final List<CuboidDepth> list = new ArrayList<CuboidDepth>();
        list.add(new CuboidDepth(node.geometry().mbc(), depth));
        if (node instanceof Leaf) {
            final Leaf<T, S> leaf = (Leaf<T, S>) node;
            for (final Entry<T, S> entry : leaf.entries()) {
                list.add(new CuboidDepth(entry.geometry().mbc(), depth + 2));
            }
        } else {
            final NonLeaf<T, S> n = (NonLeaf<T, S>) node;
            for (final Node<T, S> child : n.children()) {
                list.addAll(getRectangleDepths(child, depth + 1));
            }
        }
        return list;
    }

    private void drawNode(Graphics2D g, List<CuboidDepth> nodes) {
        for (final CuboidDepth node : nodes) {
            final Color color = Color.getHSBColor(node.getDepth() / (maxDepth + 1f), 1f, 1f);
            g.setStroke(new BasicStroke(Math.max(0.5f, maxDepth - node.getDepth() + 1 - 1)));
            g.setColor(color);
            final Cuboid r = node.getCuboid();
            drawRectangle(g, r);
        }
    }

    //TODO implement 3d drawing logic here
    private void drawRectangle(Graphics2D g, Cuboid r) {
        final double x1 = (r.x1() - view.x1()) / (view.x2() - view.x1()) * width;
        final double y1 = (r.y1() - view.y1()) / (view.y2() - view.y1()) * height;
        final double x2 = (r.x2() - view.x1()) / (view.x2() - view.x1()) * width;
        final double y2 = (r.y2() - view.y1()) / (view.y2() - view.y1()) * height;
        g.drawRect(rnd(x1), rnd(y1), rnd(x2 - x1), rnd(y2 - y1));
    }

    private static int rnd(double d) {
        return (int) Math.round(d);
    }

    public void save(File file, String imageFormat) {
        ImageSaver.save(createImage(), file, imageFormat);
    }

    public void save(String filename, String imageFormat) {
        save(new File(filename), imageFormat);
    }

    public void save(String filename) {
        save(new File(filename), "PNG");
    }
}
