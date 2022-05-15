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
import org.medal.graph.Edge.Link;
import org.medal.graph.impl.EdgeImpl;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author skrymets
 */
public class NodeTest {

    protected GraphImpl graph;

    public NodeTest() {
    }

    @Before
    public void prepareData() {
        graph = new GraphImpl();
        graph.getNodes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEdges() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        // There is nothing connected yet
        assertNotNull(node1.getEdges());
        assertTrue(node1.getEdges().isEmpty());

        EdgeImpl connection = node1.connect(node2);

        assertFalse(node1.getEdges().isEmpty());
        assertFalse(node2.getEdges().isEmpty());

        // EdgeImpl is connected to the nodes
        assertSame(connection, node1.getEdges().iterator().next());
        assertSame(node1.getEdges().iterator().next(), node2.getEdges().iterator().next());

        node1.getEdges().add(connection);
        fail("Should throw UnsupportedOperationException");
    }

    @Test
    public void testGetOutgoingEdges() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        NodeImpl node3 = nodes.get(2);

        // [node2] ----- [node1] -------> [node3]
        // Create new un/directed connections
        node1.connect(node2);
        EdgeImpl outEdge = node1.connectNodeFromRight(node3);

        // Now there are some new connections
        Collection<EdgeImpl> outEdges = node1.getOutgoingEdges();

        assertTrue(outEdges.size() == 1);
        assertTrue(outEdges.contains(outEdge));

        outEdges = node1.getOutgoingEdges(true);
        assertTrue(outEdges.size() == 2);
    }

    @Test
    public void testGetIncomingEdges() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);
        NodeImpl node3 = nodes.get(2);

        // [node2] ------> [node1] ----- [node3]
        // Create new un/directed connections
        EdgeImpl inEdge = node1.connectNodeFromLeft(node2);
        node1.connect(node3);

        // Now there are some new connections
        Collection<EdgeImpl> inEdges = node1.getIncomingEdges();

        assertTrue(inEdges.size() == 1);
        assertTrue(inEdges.contains(inEdge));

        inEdges = node1.getIncomingEdges(true);
        assertTrue(inEdges.size() == 2);
    }

    @Test
    public void testGetGraph() {

        NodeImpl node = graph.createNode();
        assertNotNull(node.getGraph());
        assertSame(node.getGraph(), graph);

    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodeFromLeft() {
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        NodeImpl leftNode = nodes.get(0);
        NodeImpl rightNode = nodes.get(1);

        EdgeImpl edge = rightNode.connectNodeFromLeft(leftNode);
        assertTrue(edge.getDirected() == Link.DIRECTED);
        assertSame(edge.getLeft(), leftNode);
        assertSame(edge.getRight(), rightNode);

        rightNode.connectNodeFromLeft(null);
        fail("Should throw NullPointerException");

    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodeFromRight() {
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        NodeImpl leftNode = nodes.get(0);
        NodeImpl rightNode = nodes.get(1);

        EdgeImpl edge = leftNode.connectNodeFromRight(rightNode);
        assertTrue(edge.getDirected() == Link.DIRECTED);
        assertSame(edge.getLeft(), leftNode);
        assertSame(edge.getRight(), rightNode);

        leftNode.connectNodeFromRight(null);
        fail("Should throw NullPointerException");
    }

    @Test
    public void testConnectNodeFromLeftAndRight() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        NodeImpl testNode = nodes.get(0);
        NodeImpl leftNode = nodes.get(1);
        NodeImpl rightNode = nodes.get(2);

        // Initially there are no connections
        Collection<EdgeImpl> emptyIncomingEdges = testNode.getIncomingEdges();
        Collection<EdgeImpl> emptyOutgoingEdges = testNode.getOutgoingEdges();

        assertNotNull(emptyIncomingEdges);
        assertNotNull(emptyOutgoingEdges);

        assertTrue(emptyIncomingEdges.isEmpty());
        assertTrue(emptyOutgoingEdges.isEmpty());

        // Create new directed connections
        EdgeImpl inEdge = testNode.connectNodeFromLeft(leftNode);
        EdgeImpl outEdge = testNode.connectNodeFromRight(rightNode);

        assertTrue(inEdge.getDirected() == Link.DIRECTED);
        assertTrue(outEdge.getDirected() == Link.DIRECTED);

        // Now there are some new connections
        Collection<EdgeImpl> incomingEdges = testNode.getIncomingEdges();
        Collection<EdgeImpl> outgoingEdges = testNode.getOutgoingEdges();

        assertNotNull(incomingEdges);
        assertNotNull(outgoingEdges);

        assertFalse(incomingEdges.isEmpty());
        assertFalse(outgoingEdges.isEmpty());

        // those collections are immutable, and point to the different sets.
        assertNotEquals(emptyIncomingEdges, incomingEdges);
        assertNotEquals(emptyOutgoingEdges, outgoingEdges);

    }

    @Test
    public void testConnect() {
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        NodeImpl leftNode = nodes.get(0);
        NodeImpl rightNode = nodes.get(1);

        EdgeImpl edge = leftNode.connect(rightNode);
        assertTrue(edge.getDirected() == Link.UNDIRECTED);

        assertSame(edge.getLeft(), leftNode);
        assertSame(edge.getRight(), rightNode);

        assertFalse(graph.getEdges().isEmpty());
        assertTrue(graph.getEdges().size() == 1);

        assertTrue(leftNode.getIncomingEdges().isEmpty());
        assertTrue(leftNode.getOutgoingEdges().isEmpty());

        assertFalse(rightNode.getIncomingEdges(true).isEmpty());
        assertFalse(rightNode.getOutgoingEdges(true).isEmpty());

    }

    @Test
    public void testGetLinkedNodes() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        NodeImpl leftNode = nodes.get(0);
        NodeImpl middleNode = nodes.get(1);
        NodeImpl rightNode = nodes.get(2);

        //
        //  [leftNode] --> [middleNode] <-- [rightNode]
        //     \_______________________________/
        //
        leftNode.connectNodeFromRight(middleNode);
        leftNode.connect(rightNode);
        middleNode.connectNodeFromLeft(rightNode);

        final Set<NodeImpl> linkedToTheLeftNode = leftNode.getLinkedNodes();
        final Set<NodeImpl> linkedToTheRightNode = rightNode.getLinkedNodes();

        assertNotNull(linkedToTheLeftNode);
        assertTrue(linkedToTheLeftNode.size() == 2);
        assertTrue(linkedToTheLeftNode.contains(middleNode) && linkedToTheLeftNode.contains(rightNode));

        assertNotNull(linkedToTheRightNode);
        assertTrue(linkedToTheRightNode.size() == 2);
        assertTrue(linkedToTheRightNode.contains(middleNode) && linkedToTheRightNode.contains(leftNode));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEdgesToNode() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        NodeImpl nodeOne = nodes.get(0);
        NodeImpl nodeTwo = nodes.get(1);
        NodeImpl nodeThree = nodes.get(2);

        //
        //  [nodeOne] --(1)--> [nodeTwo]--(4)-\
        //      \   \__(2)____/                \
        //       \_____(3)________________ [nodeThree] 
        //
        EdgeImpl one2two1 = nodeOne.connectNodeFromRight(nodeTwo); // Directed
        EdgeImpl one2two2 = nodeOne.connect(nodeTwo); // Underected
        EdgeImpl one2three1 = nodeOne.connect(nodeThree); // Underected

        assertNotSame(one2two1, one2two2);

        Set<EdgeImpl> one2twoX = nodeOne.getEdgesToNode(nodeTwo);
        assertNotNull(one2twoX);
        assertTrue(one2twoX.size() == 2);

        assertTrue(one2twoX.containsAll(Arrays.asList(one2two1, one2two2)));
        assertFalse(one2twoX.containsAll(Arrays.asList(one2three1)));

        one2twoX.add(one2three1);
        fail("Should throw UnsupportedOperationException");

    }

}
