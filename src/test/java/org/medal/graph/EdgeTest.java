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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
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

        graph.createNodes(10);

        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());
        assertEquals(10, nodes.size());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        EdgeImpl edge = node1.connect(node2);
        assertNotNull(edge.getGraph());
        assertSame(edge.getGraph(), graph);
    }

    @Test
    public void testGetDirected() {

        graph.createNodes(2);
        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        EdgeImpl edge = node1.connect(node2);
        //
        // [node1] -----(edge)----- [node2]
        //
        assertFalse(edge.isDirected());

    }

    @Test
    public void testSetDirected() {
        graph.createNodes(2);
        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        EdgeImpl edge = node1.connectNodeFromRight(node2);

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

        graph.createNodes(3);
        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        //
        // [node1] -----(edge1to2)----- [node2]       [node3]
        //    ^________(edge2to1)_________/
        //
        EdgeImpl edge1to2 = node1.connect(node2);
        EdgeImpl edge2to1 = node2.connectNodeFromRight(node1);
        assertEquals(node1.getEdges().size(), 2);

        NodeImpl oppositeToNode2 = edge1to2.getOpposite(node2).get();
        assertSame(oppositeToNode2, node1);

        NodeImpl oppositeToNode1 = edge2to1.getOpposite(node1).get();
        assertSame(oppositeToNode1, node2);

        NodeImpl node3 = nodes.get(2);
        NodeImpl oppositeToNode3 = edge1to2.getOpposite(node3).orElse(null);
        assertNull(oppositeToNode3);
    }

    @Test
    public void testGetLeft() {
        graph.createNodes(2);
        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        EdgeImpl edge1to2 = node1.connect(node2);

        assertNotNull(edge1to2.getLeft());
        assertSame(edge1to2.getLeft(), node1);
    }

    @Test
    public void testGetRight() {
        graph.createNodes(2);
        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        EdgeImpl edge1to2 = node1.connect(node2);

        assertNotNull(edge1to2.getRight());
        assertSame(edge1to2.getRight(), node2);
    }

    @Test(expected = NullPointerException.class)
    public void testInsertMiddleNode() {
        graph.createNodes(3);
        final List<NodeImpl> nodes = new ArrayList<>(graph.getNodes());

        NodeImpl node1 = nodes.get(0);
        NodeImpl node3 = nodes.get(1);

        EdgeImpl edge1to3 = node1.connect(node3);
        //
        // [node1] -----(edge1to3:"0123456789")----- [node3]
        //

        NodeImpl node2 = nodes.get(2);
        Edge.Split split = edge1to3.insertMiddleNode(node2);

        //
        // [node1] -----(leftEdge)----- [node2] -----(rightEdge)----- [node3]
        //
        assertNotNull(split);

        Edge leftEdge = split.getLeftEdge();
        Edge rightEdge = split.getRightEdge();

        assertNotNull(leftEdge);
        assertNotNull(rightEdge);

        assertSame(leftEdge.getLeft(), node1);
        assertSame(leftEdge.getRight(), node2);

        assertSame(rightEdge.getLeft(), node2);
        assertSame(rightEdge.getRight(), node3);

        /**
         * The original edge is detached from all it's parties.
         */
        assertFalse(graph.getEdges().contains(edge1to3));
        assertFalse(node1.getEdges().contains(edge1to3));
        assertFalse(node3.getEdges().contains(edge1to3));

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

        EdgeImpl edge1to2 = node1.connectNodeFromRight(node2);
        EdgeImpl edge2to3 = node2.connectNodeFromRight(node3);
        EdgeImpl edge3to4 = node3.connect(node4);
        EdgeImpl edge2to4 = node2.connectNodeFromRight(node4);

        /*
         * [Node1] --edge1to2-->> [Node2] --edge2to3-->> [Node3] --edge3to4-- [Node4]
         *                           |                                          ^
         *                           \---------------------edge2to4-------------/
         */

        assertEquals(4, graph.getEdges().size());
        assertEquals(4, graph.getNodes().size());

        final NodeImpl collapsedNode = edge2to3.collapse();

        /*
         * [Node1] --edge1to2-->> [collapsedNode] --edge3to4-- [Node4]
         *                           |                           ^
         *                           \------edge2to4-------------/
         */

        assertEquals(3, graph.getEdges().size());
        assertEquals(3, graph.getNodes().size());

        assertTrue(graph.getNodes().contains(node1));
        assertTrue(graph.getNodes().contains(collapsedNode));
        assertTrue(graph.getNodes().contains(node4));

        assertFalse(graph.getNodes().contains(node2));
        assertFalse(graph.getNodes().contains(node3));

        assertTrue(graph.getEdges().contains(edge1to2));
        assertTrue(edge1to2.isDirected());
        assertTrue(edge1to2.getRight() == collapsedNode);

        assertTrue(graph.getEdges().contains(edge3to4));
        assertFalse(edge3to4.isDirected());
        assertTrue(edge3to4.getLeft() == collapsedNode);

        assertTrue(edge2to4.isDirected());
        assertTrue(edge2to4.getLeft() == collapsedNode);
        assertTrue(edge2to4.getRight() == node4);

    }

    @Test
    public void testDuplicateMultiple() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();
        final EdgeImpl edge = node1.connectNodeFromRight(node2);

        final Collection<EdgeImpl> emptyEdgesCollection = edge.duplicate(-3);
        assertTrue(emptyEdgesCollection.isEmpty());

        final Collection<EdgeImpl> duplicatedEdges = edge.duplicate(5);
        assertEquals(5, duplicatedEdges.stream().filter(EdgeImpl::isDirected).collect(toList()).size());
    }

    @Test
    public void testDuplicate() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();

        final EdgeImpl edge = node1.connect(node2);
        assertEquals(1, graph.getEdges().size());

        final EdgeImpl duplicatedEdge = edge.duplicate();
        assertFalse(duplicatedEdge.isDirected());
        assertEquals(2, graph.getEdges().size());

        final EdgeImpl directedEdge = node1.connectNodeFromRight(node2);
        assertTrue(directedEdge.isDirected());

        final EdgeImpl duplicateDirectedEdge = directedEdge.duplicate();
        assertTrue(duplicateDirectedEdge.isDirected());

        assertEquals(directedEdge.getLeft(), duplicateDirectedEdge.getLeft());
        assertEquals(directedEdge.getRight(), duplicateDirectedEdge.getRight());

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
