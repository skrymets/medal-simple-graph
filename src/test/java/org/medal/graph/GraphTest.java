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
import org.medal.graph.impl.AbstractDataObject;
import org.medal.graph.impl.EdgeImpl;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

import java.util.*;

import static java.util.Comparator.comparing;
import static org.junit.Assert.*;

/**
 *
 * @author skrymets
 */
public class GraphTest {

    Comparator<NodeImpl> nodesComparator;
    Comparator<EdgeImpl> edgesComparator;

    public GraphTest() {
        nodesComparator = comparing(AbstractDataObject::getId);
        edgesComparator = comparing(AbstractDataObject::getId);
    }

    @Test
    public void testCreateNode() {
        Graph graph = new GraphImpl();
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
        GraphImpl graph = new GraphImpl();

        Set<NodeImpl> twoNodes = graph.createNodes(2);
        assertNotNull(twoNodes);
        assertEquals(twoNodes.size(), 2);

        assertNotNull(graph.getNodes());
        assertEquals(graph.getNodes().size(), 2);

        Collection<NodeImpl> anotherTwoNodes = graph.createNodes(2);
        assertEquals(anotherTwoNodes.size(), 2);
        assertEquals(graph.getNodes().size(), 4);

        // No edges ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        long generatedEdgesCount = graph.getNodes().stream()
                .flatMap((Node t) -> t.getEdges().stream())
                .count();
        assertEquals(generatedEdgesCount, 0L);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Set firstTwo = new TreeSet(nodesComparator);
        firstTwo.addAll(twoNodes);
        Set secondTwo = new TreeSet(nodesComparator);
        secondTwo.addAll(anotherTwoNodes);

        assertNotEquals(firstTwo, secondTwo);

        // Silently process non-positive values ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        GraphImpl graph2 = new GraphImpl();
        Set<NodeImpl> nodes = graph2.createNodes(-1000);
        assertNotNull(nodes);
        assertEquals(nodes.size(), 0);

        nodes = graph2.createNodes(0);
        assertNotNull(nodes);
        assertEquals(nodes.size(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodes() {
        GraphImpl graph = new GraphImpl();
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        final NodeImpl node1 = nodes.get(0);
        final NodeImpl node2 = nodes.get(1);

        EdgeImpl undirectedConnection1 = graph.connectNodes(node1, node2);
        assertNotNull(undirectedConnection1);
        assertEquals(undirectedConnection1.getDirected(), Edge.Link.UNDIRECTED);

        EdgeImpl undirectedConnection2 = graph.connectNodes(node1, node2, Edge.Link.UNDIRECTED);
        assertEquals(undirectedConnection2.getDirected(), Edge.Link.UNDIRECTED);

        EdgeImpl directedConnection1 = graph.connectNodes(node1, node2, Edge.Link.DIRECTED);
        assertEquals(directedConnection1.getDirected(), Edge.Link.DIRECTED);

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
        GraphImpl graph = new GraphImpl();
        Set<EdgeImpl> edges = graph.getEdges();

        assertNotNull(edges);

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        final NodeImpl node1 = nodes.get(0);
        final NodeImpl node2 = nodes.get(1);

        EdgeImpl edge1 = node1.connect(node2);
        assertEquals(graph.getEdges().size(), 1);

        EdgeImpl edge2 = node1.connect(node2);
        assertEquals(graph.getEdges().size(), 2);

        assertNotEquals(edge1, edge2);

    }

    @Test
    public void testGetNodes() {
        Graph graph = new GraphImpl();
        Collection<NodeImpl> newNodes = graph.createNodes(2);

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
        GraphImpl graph = new GraphImpl();
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));

        final NodeImpl node1 = nodes.get(0);
        final NodeImpl node2 = nodes.get(1);

        EdgeImpl edge = node1.connect(node2);
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
