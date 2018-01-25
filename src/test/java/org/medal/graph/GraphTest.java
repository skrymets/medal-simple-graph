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

import org.medal.graph.api.INode;
import org.medal.graph.api.IEdge;
import org.medal.graph.api.IGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author skrymets
 */
public class GraphTest {

    Comparator<Node> nodesComparator;
    Comparator<Edge> edgesComparator;

    public GraphTest() {
        nodesComparator = (Node o1, Node o2) -> {
            return o1.getId().compareTo(o2.getId());
        };
        edgesComparator = (Edge o1, Edge o2) -> {
            return o1.getId().compareTo(o2.getId());
        };
    }

    @Test
    public void testCreateNode() {
        Graph graph = new Graph();
        Node node1 = graph.createNode();

        assertNotNull(node1);
        assertNotNull(node1.getId());
        assertNull(node1.getData());

        Node node2 = graph.createNode();
        assertNotEquals(node1.getId(), node2.getId());

        // allow null as a payload
        Node node3 = graph.createNode(null);
        assertNull(node3.getData());

        Node node4 = graph.createNode(new Object());
        assertNotNull(node4.getData());
    }

    @Test
    public void testCreateNodes() {
        Graph graph = new Graph();

        Set<Node> twoNodes = graph.createNodes(2);
        assertNotNull(twoNodes);
        assertEquals(twoNodes.size(), 2);

        assertNotNull(graph.getNodes());
        assertEquals(graph.getNodes().size(), 2);

        Collection<Node> anotherTwoNodes = graph.createNodes(2);
        assertEquals(anotherTwoNodes.size(), 2);
        assertEquals(graph.getNodes().size(), 4);

        // No edges ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        long generatedEdgesCount = graph.getEdges().size();
        assertEquals(generatedEdgesCount, 0L);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Set firstTwo = new TreeSet(nodesComparator);
        firstTwo.addAll(twoNodes);
        Set secondTwo = new TreeSet(nodesComparator);
        secondTwo.addAll(anotherTwoNodes);

        assertNotEquals(firstTwo, secondTwo);

        // Silently process non-positive values ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Graph graph2 = new Graph();
        Set<Node> nodes = graph2.createNodes(-1000);
        assertNotNull(nodes);
        assertEquals(nodes.size(), 0);

        nodes = graph2.createNodes(0);
        assertNotNull(nodes);
        assertEquals(nodes.size(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodes() {
        Graph graph = new Graph();
        List<Node> nodes = new ArrayList<>(graph.createNodes(2));
        final Node node1 = nodes.get(0);
        final Node node2 = nodes.get(1);

        Edge undirectedConnection1 = graph.connectNodes(node1, node2);
        assertNotNull(undirectedConnection1);
        assertEquals(undirectedConnection1.getDirected(), IEdge.Link.UNDIRECTED);

        Edge undirectedConnection2 = graph.connectNodes(node1, node2, IEdge.Link.UNDIRECTED);
        assertEquals(undirectedConnection2.getDirected(), IEdge.Link.UNDIRECTED);

        Edge directedConnection1 = graph.connectNodes(node1, node2, IEdge.Link.DIRECTED);
        assertEquals(directedConnection1.getDirected(), IEdge.Link.DIRECTED);

        assertNotEquals(undirectedConnection1, undirectedConnection2);
        assertNotEquals(undirectedConnection2, directedConnection1);
        assertNotEquals(undirectedConnection1, directedConnection1);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        boolean takeFirst = new Random().nextBoolean();
        graph.connectNodes(takeFirst ? node1 : null, takeFirst ? null : node2);
        fail("Should not connect undefined nodes. Must throw NullPointerException");
    }

    @Test
    public void testGetEdges() {
        Graph graph = new Graph();
        Set<Edge> edges = graph.getEdges();

        assertNotNull(edges);

        List<Node> nodes = new ArrayList<>(graph.createNodes(2));
        final Node node1 = nodes.get(0);
        final Node node2 = nodes.get(1);

        Edge edge1 = node1.connect(node2);
        assertEquals(graph.getEdges().size(), 1);

        Edge edge2 = node1.connect(node2);
        assertEquals(graph.getEdges().size(), 2);

        assertNotEquals(edge1, edge2);

    }

    @Test
    public void testGetNodes() {
        IGraph graph = new Graph();
        Collection<Node> newNodes = graph.createNodes(2);

        assertNotNull(graph.getNodes());
        assertFalse(graph.getNodes().isEmpty());

        Set existingNodes = new TreeSet(nodesComparator);
        existingNodes.addAll(graph.getNodes());
        Set createdNodes = new TreeSet(nodesComparator);
        createdNodes.addAll(newNodes);

        assertEquals(existingNodes, createdNodes);
    }

    @Test
    public void testBreakEdge() {
        Graph graph = new Graph();
        List<Node> nodes = new ArrayList<>(graph.createNodes(2));

        final Node node1 = nodes.get(0);
        final Node node2 = nodes.get(1);

        Edge edge = node1.connect(node2);
        assertNotNull(edge);

        // NOT fails on null
        graph.breakEdge(null);
        graph.breakEdge(edge);

        // Still refers the nodes ... ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        assertSame(edge.getLeft(), node1);
        assertSame(edge.getRight(), node2);

        // ... but neither the former do this ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        assertFalse(graph.getEdges().contains(edge));
        assertFalse(node1.getEdges().contains(edge));
        assertFalse(node2.getEdges().contains(edge));
    }

}
