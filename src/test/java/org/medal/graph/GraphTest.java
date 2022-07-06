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

import org.junit.Test;
import org.medal.graph.impl.EdgeImpl;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

import java.util.*;

import static java.util.Comparator.comparing;
import static org.junit.Assert.*;

/**
 * @author skrymets
 */
public class GraphTest {

    private Comparator<NodeImpl> nodesComparator;

    public GraphTest() {
        nodesComparator = comparing(NodeImpl::hashCode);
    }

    @Test
    public void testCreateNode() {
        GraphImpl graph = new GraphImpl();
        NodeImpl node1 = graph.createNode();

        assertNotNull(node1);
    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodes() {
        GraphImpl graph = new GraphImpl();
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();

        EdgeImpl undirectedConnection1 = graph.connect(node1, node2);
        assertNotNull(undirectedConnection1);
        assertFalse(undirectedConnection1.isDirected());

        EdgeImpl undirectedConnection2 = graph.connect(node1, node2, false);
        assertFalse(undirectedConnection2.isDirected());

        EdgeImpl directedConnection1 = graph.connect(node1, node2, true);
        assertTrue(directedConnection1.isDirected());

        assertNotEquals(undirectedConnection1, undirectedConnection2);
        assertNotEquals(undirectedConnection2, directedConnection1);
        assertNotEquals(undirectedConnection1, directedConnection1);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        boolean takeFirst = new Random().nextBoolean();
        graph.connect(takeFirst ? node1 : null, takeFirst ? null : node2);
        fail("Should not connect undefined nodes. Must throw NullPointerException");
    }

    @Test
    public void testGetEdges() {
        GraphImpl graph = new GraphImpl();
        Set<EdgeImpl> edges = graph.edges();

        assertNotNull(edges);

        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();

        EdgeImpl edge1 = node1.connect(node2);
        assertEquals(graph.edges().size(), 1);

        EdgeImpl edge2 = node1.connect(node2);
        assertEquals(graph.edges().size(), 2);

        assertNotEquals(edge1, edge2);

    }

    @Test
    public void testGetNodes() {
        Graph<NodeImpl, EdgeImpl> graph = new GraphImpl();
        Collection<NodeImpl> newNodes = List.of(graph.createNode(), graph.createNode());

        assertNotNull(graph.nodes());
        assertFalse(graph.nodes().isEmpty());

        Set<NodeImpl> existingNodes = new TreeSet<>(nodesComparator);
        existingNodes.addAll(graph.nodes());
        Set<NodeImpl> createdNodes = new TreeSet<>(nodesComparator);
        createdNodes.addAll(newNodes);

        assertEquals(existingNodes, createdNodes);
    }

    @Test
    public void testBreakEdge() {
        GraphImpl graph = new GraphImpl();

        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();

        EdgeImpl edge = node1.connect(node2);
        assertNotNull(edge);

        // NOT fails on null
        graph.deleteEdge(null);
        graph.deleteEdge(edge);

        // Still refers the nodes ... ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        assertSame(edge.left(), node1);
        assertSame(edge.right(), node2);

        // ... but neither the former do this ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        assertFalse(graph.edges().contains(edge));
        assertFalse(node1.incidentEdges().contains(edge));
        assertFalse(node2.incidentEdges().contains(edge));
    }

    @Test
    public void testDeleteNodes() {
        GraphImpl graph = new GraphImpl();
        /*
         *   (0) ----- (1)
         *    |  \   /  |
         *    |    X    |
         *    |  /   \  |
         *   (3) ----- (2)
         */
        final NodeImpl node0 = graph.createNode();
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();
        final NodeImpl node3 = graph.createNode();

        node0.connect(node1);
        node1.connect(node2);
        node2.connect(node3);
        node3.connect(node0);
        node1.connect(node3);
        node0.connect(node2);

        assertEquals(6, graph.edges().size());
        assertEquals(4, graph.nodes().size());

        graph.deleteNode((NodeImpl) null);
        assertEquals(6, graph.edges().size());
        assertEquals(4, graph.nodes().size());

        graph.deleteNode(node2);
        /*
         *   (0) ----- (1)
         *    |      /
         *    |    /
         *    |  /
         *   (3)
         */
        assertEquals(3, graph.edges().size());
        assertEquals(3, graph.nodes().size());

    }

}
