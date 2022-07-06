/*
 * Copyright 2017 skrymets.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.medal.graph;

import org.junit.Before;
import org.junit.Test;
import org.medal.graph.impl.EdgeImpl;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

import static org.junit.Assert.*;

/**
 * @author skrymets
 */
public class EdgeTest {

    protected GraphImpl graph;

    public EdgeTest() {
    }

    @Before
    public void prepareData() {
        graph = new GraphImpl();
    }

    @Test
    public void testGetGraph() {


        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        assertEquals(2, graph.nodes().size());

        EdgeImpl edge = node1.connect(node2);
        assertNotNull(edge.graph());
        assertSame(edge.graph(), graph);
    }

    @Test
    public void testGetDirected() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();

        EdgeImpl edge = node1.connect(node2);
        //
        // [node1] -----(edge)----- [node2]
        //
        assertFalse(edge.isDirected());

    }

    @Test
    public void testSetDirected() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();

        EdgeImpl edge = node1.connectAsTarget(node2);

        //
        // [node1] -----(edge)----> [node2]
        //       
        assertTrue(edge.isDirected());

        EdgeImpl retEdge = edge.setUndirected();

        //
        // [node1] -----(edge)----- [node2]
        //
        assertFalse(edge.isDirected());

        assertSame(edge, retEdge);

    }

    @Test
    public void testGetOpposite() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();

        //
        // [node1] -----(edge1to2)----- [node2]       [node3]
        //    ^________(edge2to1)_________/
        //
        EdgeImpl edge1to2 = node1.connect(node2);
        EdgeImpl edge2to1 = node2.connectAsTarget(node1);
        assertEquals(node1.edges().size(), 2);

        NodeImpl oppositeToNode2 = edge1to2.opposite(node2).get();
        assertSame(oppositeToNode2, node1);

        NodeImpl oppositeToNode1 = edge2to1.opposite(node1).get();
        assertSame(oppositeToNode1, node2);

        NodeImpl node3 = graph.createNode();
        NodeImpl oppositeToNode3 = edge1to2.opposite(node3).orElse(null);
        assertNull(oppositeToNode3);
    }

    @Test
    public void testGetLeft() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        EdgeImpl edge1to2 = node1.connect(node2);

        assertNotNull(edge1to2.left());
        assertSame(edge1to2.left(), node1);
    }

    @Test
    public void testGetRight() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        EdgeImpl edge1to2 = node1.connect(node2);

        assertNotNull(edge1to2.right());
        assertSame(edge1to2.right(), node2);
    }

    @Test()
    public void testInsertNewMiddleNode() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();

        EdgeImpl edge1to2 = node1.connect(node2);
        //
        // [node1] -----(edge1to2)----- [node2]
        //

        assertEquals(2, graph.nodes().size());
        assertEquals(1, graph.edges().size());

        final Edge.Split<NodeImpl, EdgeImpl> split = edge1to2.insertMiddleNode();
        //
        // [node1] -----(edge1)----- [middle] -----(edge2)----- [node2]
        //

        assertEquals(3, graph.nodes().size());
        assertEquals(2, graph.edges().size());

        assertSame(node1, split.leftEdge().left());
        assertSame(node2, split.rightEdge().right());
    }

    @Test(expected = NullPointerException.class)
    public void testInsertMiddleNode() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node3 = graph.createNode();

        EdgeImpl edge1to3 = node1.connect(node3);
        //
        // [node1] -----(edge1to3)----- [node3]
        //

        NodeImpl node2 = graph.createNode();
        Edge.Split split = edge1to3.insertMiddleNode(node2);

        //
        // [node1] -----(leftEdge)----- [node2] -----(rightEdge)----- [node3]
        //
        assertNotNull(split);

        Edge leftEdge = split.leftEdge();
        Edge rightEdge = split.rightEdge();

        assertNotNull(leftEdge);
        assertNotNull(rightEdge);

        assertSame(leftEdge.left(), node1);
        assertSame(leftEdge.right(), node2);

        assertSame(rightEdge.left(), node2);
        assertSame(rightEdge.right(), node3);

        /**
         * The original edge is detached from all it's parties.
         */
        assertFalse(graph.edges().contains(edge1to3));
        assertFalse(node1.edges().contains(edge1to3));
        assertFalse(node3.edges().contains(edge1to3));

        /**
         * Do not accept an undefined node
         */
        leftEdge.insertMiddleNode(null);
        fail("Should throw a NullPointerException");
    }

    @Test()
    public void testCollapseEdge() {

        Graph<NodeImpl, EdgeImpl> graph = new GraphImpl();
        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        NodeImpl node3 = graph.createNode();
        NodeImpl node4 = graph.createNode();

        EdgeImpl edge1to2 = node1.connectAsTarget(node2);
        EdgeImpl edge2to3 = node2.connectAsTarget(node3);
        EdgeImpl edge3to4 = node3.connect(node4);
        EdgeImpl edge2to4 = node2.connectAsTarget(node4);

        /*
         * [Node1] --edge1to2-->> [Node2] --edge2to3-->> [Node3] --edge3to4-- [Node4]
         *                           |                                          ^
         *                           \---------------------edge2to4-------------/
         */

        assertEquals(4, graph.edges().size());
        assertEquals(4, graph.nodes().size());

        final NodeImpl collapsedNode = edge2to3.collapse();

        /*
         * [Node1] --edge1to2-->> [collapsedNode] --edge3to4-- [Node4]
         *                           |                           ^
         *                           \------edge2to4-------------/
         */

        assertEquals(3, graph.edges().size());
        assertEquals(3, graph.nodes().size());

        assertTrue(graph.nodes().contains(node1));
        assertTrue(graph.nodes().contains(collapsedNode));
        assertTrue(graph.nodes().contains(node4));

        assertFalse(graph.nodes().contains(node2));
        assertFalse(graph.nodes().contains(node3));

        assertTrue(graph.edges().contains(edge1to2));
        assertTrue(edge1to2.isDirected());
        assertTrue(edge1to2.right() == collapsedNode);

        assertTrue(graph.edges().contains(edge3to4));
        assertFalse(edge3to4.isDirected());
        assertTrue(edge3to4.left() == collapsedNode);

        assertTrue(edge2to4.isDirected());
        assertTrue(edge2to4.left() == collapsedNode);
        assertTrue(edge2to4.right() == node4);

    }

    @Test
    public void testDuplicate() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();

        final EdgeImpl edge = node1.connect(node2);
        assertEquals(1, graph.edges().size());

        final EdgeImpl duplicatedEdge = edge.duplicate();
        assertFalse(duplicatedEdge.isDirected());
        assertEquals(2, graph.edges().size());

        final EdgeImpl directedEdge = node1.connectAsTarget(node2);
        assertTrue(directedEdge.isDirected());

        final EdgeImpl duplicateDirectedEdge = directedEdge.duplicate();
        assertTrue(duplicateDirectedEdge.isDirected());

        assertEquals(directedEdge.left(), duplicateDirectedEdge.left());
        assertEquals(directedEdge.right(), duplicateDirectedEdge.right());

    }

    @Test
    public void testDirected() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();
        final EdgeImpl edge = node1.connect(node2);

        assertFalse(edge.isDirected());

        edge.setDirected();
        assertTrue(edge.isDirected());

    }
}
