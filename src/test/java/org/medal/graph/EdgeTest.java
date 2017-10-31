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

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.medal.graph.Edge.Link;
import org.medal.graph.impl.EdgeImpl;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

/**
 *
 * @author skrymets
 */
public class EdgeTest {

    protected GraphImpl graph;

    protected List<NodeImpl> nodes;

    protected static final int INITIAL_NODES_COUNT = 10;

    public EdgeTest() {
    }

    @Before
    public void prepareData() {
        graph = new GraphImpl();
        nodes = new ArrayList<>(graph.createNodes(INITIAL_NODES_COUNT));
    }

    @Test
    public void testGetGraph() {

        assertEquals(nodes.size(), INITIAL_NODES_COUNT);

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        EdgeImpl edge = node1.connect(node2);
        assertNotNull(edge.getGraph());
        assertSame(edge.getGraph(), graph);

    }

    @Test
    public void testGetDirected() {

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        EdgeImpl edge = node1.connect(node2);

        assertNotNull(edge.getDirected());
        assertEquals(edge.getDirected(), Link.UNDIRECTED);

    }

    @Test
    public void testSetDirected() {

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        EdgeImpl edge = node1.connectNodeFromRight(node2);

        assertNotNull(edge.getDirected());
        assertEquals(edge.getDirected(), Link.DIRECTED);

        EdgeImpl retEdge = edge.setDirected(null);
        assertNotNull(edge.getDirected());
        assertEquals(edge.getDirected(), Link.UNDIRECTED);

        assertSame(edge, retEdge);
        
    }

    @Test
    public void testGetOpposite() {

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        //
        // [node1] ---------- [node2]       [node3]
        //    ^_________________/
        //
        EdgeImpl edge1to2 = node1.connect(node2);
        EdgeImpl edge2to1 = node2.connectNodeFromRight(node1);
        assertEquals(node1.getEdges().size(), 2);

        NodeImpl oppositeToNode2 = edge1to2.getOpposite(node2);
        assertSame(oppositeToNode2, node1);

        NodeImpl oppositeToNode1 = edge2to1.getOpposite(node1);
        assertSame(oppositeToNode1, node2);

        NodeImpl node3 = nodes.get(3);
        NodeImpl oppositeToNode3 = edge1to2.getOpposite(node3);
        assertNull(oppositeToNode3);
    }

    @Test
    public void testGetLeft() {
        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        EdgeImpl edge1to2 = node1.connect(node2);

        assertNotNull(edge1to2.getLeft());
        assertSame(edge1to2.getLeft(), node1);
    }

    @Test
    public void testGetRight() {
        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        EdgeImpl edge1to2 = node1.connect(node2);

        assertNotNull(edge1to2.getRight());
        assertSame(edge1to2.getRight(), node2);
    }
}
