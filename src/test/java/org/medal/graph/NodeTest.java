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

import java.util.Collection;
import java.util.Set;

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
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEdges() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        assertEquals(2, graph.nodes().size());

        // There is nothing connected yet
        assertNotNull(node1.edges());
        assertTrue(node1.edges().isEmpty());

        EdgeImpl connection = node1.connect(node2);

        assertFalse(node1.edges().isEmpty());
        assertFalse(node2.edges().isEmpty());

        // EdgeImpl is connected to the nodes
        assertSame(connection, node1.edges().iterator().next());
        assertSame(node1.edges().iterator().next(), node2.edges().iterator().next());

        node1.edges().add(connection);
        fail("Should throw UnsupportedOperationException");
    }

    @Test
    public void testGetOutgoingEdges() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        NodeImpl node3 = graph.createNode();

        // [node2] ----- [node1] -------> [node3]
        // Create new un/directed connections
        node1.connect(node2);
        EdgeImpl outEdge = node1.connectAsTarget(node3);

        // Now there are some new connections
        Collection<EdgeImpl> outEdges = node1.outgoingEdges();

        assertTrue(outEdges.size() == 1);
        assertTrue(outEdges.contains(outEdge));

        outEdges = node1.outgoingEdges(true);
        assertTrue(outEdges.size() == 2);
    }

    @Test
    public void testGetIncomingEdges() {

        NodeImpl node1 = graph.createNode();
        NodeImpl node2 = graph.createNode();
        NodeImpl node3 = graph.createNode();

        // [node2] ------> [node1] ----- [node3]
        // Create new un/directed connections
        EdgeImpl inEdge = node1.connectAsSource(node2);
        node1.connect(node3);

        // Now there are some new connections
        Collection<EdgeImpl> inEdges = node1.incomingEdges();

        assertTrue(inEdges.size() == 1);
        assertTrue(inEdges.contains(inEdge));

        inEdges = node1.incomingEdges(true);
        assertTrue(inEdges.size() == 2);
    }

    @Test
    public void testGetGraph() {

        NodeImpl node = graph.createNode();
        assertNotNull(node.graph());
        assertSame(node.graph(), graph);

    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodeFromLeft() {

        NodeImpl leftNode = graph.createNode();
        NodeImpl rightNode = graph.createNode();

        EdgeImpl edge = rightNode.connectAsSource(leftNode);
        assertTrue(edge.isDirected());
        assertSame(edge.left(), leftNode);
        assertSame(edge.right(), rightNode);

        rightNode.connectAsSource(null);
        fail("Should throw NullPointerException");

    }

    @Test(expected = NullPointerException.class)
    public void testConnectNodeFromRight() {

        NodeImpl leftNode = graph.createNode();
        NodeImpl rightNode = graph.createNode();

        EdgeImpl edge = leftNode.connectAsTarget(rightNode);
        assertTrue(edge.isDirected());
        assertSame(edge.left(), leftNode);
        assertSame(edge.right(), rightNode);

        leftNode.connectAsTarget(null);
        fail("Should throw NullPointerException");
    }

    @Test
    public void testConnectNodeFromLeftAndRight() {

        NodeImpl testNode = graph.createNode();
        NodeImpl leftNode = graph.createNode();
        NodeImpl rightNode = graph.createNode();

        // Initially there are no connections
        Collection<EdgeImpl> emptyIncomingEdges = testNode.incomingEdges();
        Collection<EdgeImpl> emptyOutgoingEdges = testNode.outgoingEdges();

        assertNotNull(emptyIncomingEdges);
        assertNotNull(emptyOutgoingEdges);

        assertTrue(emptyIncomingEdges.isEmpty());
        assertTrue(emptyOutgoingEdges.isEmpty());

        // Create new directed connections
        EdgeImpl inEdge = testNode.connectAsSource(leftNode);
        EdgeImpl outEdge = testNode.connectAsTarget(rightNode);

        assertTrue(inEdge.isDirected());
        assertTrue(outEdge.isDirected());

        // Now there are some new connections
        Collection<EdgeImpl> incomingEdges = testNode.incomingEdges();
        Collection<EdgeImpl> outgoingEdges = testNode.outgoingEdges();

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

        NodeImpl leftNode = graph.createNode();
        NodeImpl rightNode = graph.createNode();

        EdgeImpl edge = leftNode.connect(rightNode);
        assertFalse(edge.isDirected());

        assertSame(edge.left(), leftNode);
        assertSame(edge.right(), rightNode);

        assertFalse(graph.edges().isEmpty());
        assertTrue(graph.edges().size() == 1);

        assertTrue(leftNode.incomingEdges().isEmpty());
        assertTrue(leftNode.outgoingEdges().isEmpty());

        assertFalse(rightNode.incomingEdges(true).isEmpty());
        assertFalse(rightNode.outgoingEdges(true).isEmpty());

    }

    @Test
    public void testGetLinkedNodes() {

        NodeImpl leftNode = graph.createNode();
        NodeImpl middleNode = graph.createNode();
        NodeImpl rightNode = graph.createNode();

        //
        //  [leftNode] --> [middleNode] <-- [rightNode]
        //     \_______________________________/
        //
        leftNode.connectAsTarget(middleNode);
        leftNode.connect(rightNode);
        middleNode.connectAsSource(rightNode);

        final Set<NodeImpl> linkedToTheLeftNode = leftNode.linkedNodes();
        final Set<NodeImpl> linkedToTheRightNode = rightNode.linkedNodes();

        assertNotNull(linkedToTheLeftNode);
        assertTrue(linkedToTheLeftNode.size() == 2);
        assertTrue(linkedToTheLeftNode.contains(middleNode) && linkedToTheLeftNode.contains(rightNode));

        assertNotNull(linkedToTheRightNode);
        assertTrue(linkedToTheRightNode.size() == 2);
        assertTrue(linkedToTheRightNode.contains(middleNode) && linkedToTheRightNode.contains(leftNode));
    }

}
