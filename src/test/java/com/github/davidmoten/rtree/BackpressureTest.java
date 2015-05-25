package com.github.davidmoten.rtree;

import static com.github.davidmoten.rtree.RTreeTest.e;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Cuboid;
import com.github.davidmoten.util.ImmutableStack;
import com.github.davidmoten.util.TestingUtil;

public class BackpressureTest {

    @Test
    public void testConstructorIsPrivate() {
        TestingUtil.callConstructorAndCheckIsPrivate(Backpressure.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBackpressureSearch() {
        Subscriber<Object> sub = Mockito.mock(Subscriber.class);
        ImmutableStack<NodePosition<Object, Geometry>> stack = ImmutableStack.empty();
        Func1<Geometry, Boolean> condition = Mockito.mock(Func1.class);
        Backpressure.search(condition, sub, stack, 1);
        Mockito.verify(sub, Mockito.never()).onNext(Mockito.any());
    }

    @Test
    public void testBackpressureSearchNodeWithConditionThatAlwaysReturnsFalse() {
        RTree<Object, Cuboid> tree = RTree.maxChildren(3).<Object, Cuboid> create().add(e(1))
                .add(e(3)).add(e(5)).add(e(7));

        Set<Entry<Object, Cuboid>> found = new HashSet<Entry<Object, Cuboid>>();
        tree.search(e(1).geometry()).subscribe(backpressureSubscriber(found));
        assertEquals(1, found.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRequestZero() {
        Subscriber<Object> sub = new Subscriber<Object>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object t) {

            }
        };
        sub.add(new Subscription() {
            volatile boolean subscribed = true;

            @Override
            public void unsubscribe() {
                subscribed = false;
            }

            @Override
            public boolean isUnsubscribed() {
                return !subscribed;
            }
        });
        Node<Object, Geometry> node = Mockito.mock(Node.class);
        NodePosition<Object, Geometry> np = new NodePosition<Object, Geometry>(node, 1);
        ImmutableStack<NodePosition<Object, Geometry>> stack = ImmutableStack
                .<NodePosition<Object, Geometry>> empty().push(np);
        Func1<Geometry, Boolean> condition = Mockito.mock(Func1.class);
        ImmutableStack<NodePosition<Object, Geometry>> stack2 = Backpressure.search(condition, sub,
                stack, 0);
        assertTrue(stack2 == stack);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRequestZeroWhenUnsubscribed() {
        Subscriber<Object> sub = new Subscriber<Object>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object t) {

            }
        };
        sub.add(new Subscription() {

            volatile boolean subscribed = true;

            @Override
            public void unsubscribe() {
                subscribed = false;
            }

            @Override
            public boolean isUnsubscribed() {
                return !subscribed;
            }
        });
        sub.unsubscribe();
        Node<Object, Geometry> node = Mockito.mock(Node.class);
        NodePosition<Object, Geometry> np = new NodePosition<Object, Geometry>(node, 1);
        ImmutableStack<NodePosition<Object, Geometry>> stack = ImmutableStack
                .<NodePosition<Object, Geometry>> empty().push(np);
        Func1<Geometry, Boolean> condition = Mockito.mock(Func1.class);
        ImmutableStack<NodePosition<Object, Geometry>> stack2 = Backpressure.search(condition, sub,
                stack, 1);
        assertTrue(stack2.isEmpty());
    }

    @Test
    public void testBackpressureIterateWhenNodeHasMaxChildrenAndIsRoot() {
        Entry<Object, Cuboid> e1 = RTreeTest.e(1);
        @SuppressWarnings("unchecked")
        List<Entry<Object, Cuboid>> list = Arrays.asList(e1, e1, e1, e1);
        RTree<Object, Cuboid> tree = RTree.star().maxChildren(4).<Object, Cuboid> create()
                .add(list);
        HashSet<Entry<Object, Cuboid>> expected = new HashSet<Entry<Object, Cuboid>>(list);
        final HashSet<Entry<Object, Cuboid>> found = new HashSet<Entry<Object, Cuboid>>();
        tree.entries().subscribe(backpressureSubscriber(found));
        assertEquals(expected, found);
    }

    @Test
    public void testBackpressureRequestZero() {
        Entry<Object, Cuboid> e1 = RTreeTest.e(1);
        @SuppressWarnings("unchecked")
        List<Entry<Object, Cuboid>> list = Arrays.asList(e1, e1, e1, e1);
        RTree<Object, Cuboid> tree = RTree.star().maxChildren(4).<Object, Cuboid> create()
                .add(list);
        HashSet<Entry<Object, Cuboid>> expected = new HashSet<Entry<Object, Cuboid>>(list);
        final HashSet<Entry<Object, Cuboid>> found = new HashSet<Entry<Object, Cuboid>>();
        tree.entries().subscribe(new Subscriber<Entry<Object, Cuboid>>() {

            @Override
            public void onStart() {
                request(1);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Entry<Object, Cuboid> t) {
                found.add(t);
                request(0);
            }
        });
        assertEquals(expected, found);
    }

    @Test
    public void testBackpressureIterateWhenNodeHasMaxChildrenAndIsNotRoot() {
        Entry<Object, Cuboid> e1 = RTreeTest.e(1);
        List<Entry<Object, Cuboid>> list = new ArrayList<Entry<Object, Cuboid>>();
        for (int i = 1; i <= 17; i++)
            list.add(e1);
        RTree<Object, Cuboid> tree = RTree.star().maxChildren(4).<Object, Cuboid> create()
                .add(list);
        HashSet<Entry<Object, Cuboid>> expected = new HashSet<Entry<Object, Cuboid>>(list);
        final HashSet<Entry<Object, Cuboid>> found = new HashSet<Entry<Object, Cuboid>>();
        tree.entries().subscribe(backpressureSubscriber(found));
        assertEquals(expected, found);
    }

    @Test
    public void testBackpressureIterateWhenConditionFailsAgainstNonLeafNode() {
        Entry<Object, Cuboid> e1 = e(1);
        List<Entry<Object, Cuboid>> list = new ArrayList<Entry<Object, Cuboid>>();
        for (int i = 1; i <= 17; i++)
            list.add(e1);
        list.add(e(2));
        RTree<Object, Cuboid> tree = RTree.star().maxChildren(4).<Object, Cuboid> create()
                .add(list);
        HashSet<Entry<Object, Cuboid>> expected = new HashSet<Entry<Object, Cuboid>>(list);
        final HashSet<Entry<Object, Cuboid>> found = new HashSet<Entry<Object, Cuboid>>();
        tree.entries().subscribe(backpressureSubscriber(found));
        assertEquals(expected, found);
    }

    @Test
    public void testBackpressureIterateWhenConditionFailsAgainstLeafNode() {
        Entry<Object, Cuboid> e3 = e(3);
        RTree<Object, Cuboid> tree = RTree.star().maxChildren(4).<Object, Cuboid> create()
                .add(e(1)).add(e3);
        Set<Entry<Object, Cuboid>> expected = Collections.singleton(e3);
        final Set<Entry<Object, Cuboid>> found = new HashSet<Entry<Object, Cuboid>>();
        tree.search(e3.geometry()).subscribe(backpressureSubscriber(found));
        assertEquals(expected, found);
    }

    private static Subscriber<Entry<Object, Cuboid>> backpressureSubscriber(
            final Set<Entry<Object, Cuboid>> found) {
        return new Subscriber<Entry<Object, Cuboid>>() {

            @Override
            public void onStart() {
                request(1);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Entry<Object, Cuboid> t) {
                found.add(t);
                request(1);
            }
        };
    }

}
