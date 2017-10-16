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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author skrymets
 */
public class NodeTest {

    protected Graph graph;

    public NodeTest() {
    }

    @Before
    public void prepareData() {
        graph = new Graph();
    }

    @Test
    public void testGetEdges() {

        List<Node> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        Node node1 = nodes.get(0);
        Node node2 = nodes.get(1);

        // There is nothing connected yet
        assertNotNull(node1.getEdges());
        assertTrue(node1.getEdges().isEmpty());

        Edge connection = node1.connect(node2);

        assertFalse(node1.getEdges().isEmpty());
        assertFalse(node2.getEdges().isEmpty());

        // Edge is connected to the nodes
        assertEquals(connection, node1.getEdges().iterator().next());
        assertEquals(node1.getEdges().iterator().next(), node2.getEdges().iterator().next());

    }

    @Test
    public void testGetIncomingAndOutgoingEdges() {

        List<Node> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        Node testNode = nodes.get(0);
        Node leftNode = nodes.get(1);
        Node rightNode = nodes.get(2);

        // Create new directed connections
        Edge inEdge = testNode.connectNodeFromLeft(leftNode);
        Edge outEdge = testNode.connectNodeFromRight(rightNode);

        // Now there are some new connections
        Collection<Edge> incomingEdges = testNode.getIncomingEdges();
        Collection<Edge> outgoingEdges = testNode.getOutgoingEdges();

        assertTrue(incomingEdges.size() == 1);
        assertTrue(incomingEdges.contains(inEdge));

        assertTrue(outgoingEdges.size() == 1);
        assertTrue(outgoingEdges.contains(outEdge));

    }

    @Test
    public void testGetGraph() {

        Node node = graph.createNode();
        assertNotNull(node.getGraph());
        assertSame(node.getGraph(), graph);

    }

    @Test
    public void testConnectNodeFromLeftAndRight() {

        List<Node> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        Node testNode = nodes.get(0);
        Node leftNode = nodes.get(1);
        Node rightNode = nodes.get(2);

        // Initially there are no connections
        Collection<Edge> emptyIncomingEdges = testNode.getIncomingEdges();
        Collection<Edge> emptyOutgoingEdges = testNode.getOutgoingEdges();

        assertNotNull(emptyIncomingEdges);
        assertNotNull(emptyOutgoingEdges);

        assertTrue(emptyIncomingEdges.isEmpty());
        assertTrue(emptyOutgoingEdges.isEmpty());

        // Create new directed connections
        Edge inEdge = testNode.connectNodeFromLeft(leftNode);
        Edge outEdge = testNode.connectNodeFromRight(rightNode);

        assertTrue(inEdge.getDirection() == Edge.Direction.DIRECT);
        assertTrue(outEdge.getDirection() == Edge.Direction.DIRECT);

        // Now there are some new connections
        Collection<Edge> incomingEdges = testNode.getIncomingEdges();
        Collection<Edge> outgoingEdges = testNode.getOutgoingEdges();

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
        List<Node> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        Node leftNode = nodes.get(0);
        Node rightNode = nodes.get(1);

        Edge edge = leftNode.connect(rightNode);
        assertTrue(edge.getDirection() == Edge.Direction.UNDIRECT);
        
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
        
        List<Node> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        Node leftNode = nodes.get(0);
        Node middleNode = nodes.get(1);
        Node rightNode = nodes.get(2);

        //
        //  [leftNode] --- [middleNode] --- [rightNode]
        //     \_______________________________/
        //
        leftNode.connect(middleNode);
        middleNode.connect(rightNode);
        leftNode.connect(rightNode);
        
        final Set<Node> linkedToTheLeftNode = leftNode.getLinkedNodes();
        final Set<Node> linkedToTheRightNode = rightNode.getLinkedNodes();
        
        assertNotNull(linkedToTheLeftNode);
        assertTrue(linkedToTheLeftNode.size() == 2);
        assertTrue(linkedToTheLeftNode.contains(middleNode) && linkedToTheLeftNode.contains(rightNode));
        
        assertNotNull(linkedToTheRightNode);
        assertTrue(linkedToTheRightNode.size() == 2);
        assertTrue(linkedToTheRightNode.contains(middleNode) && linkedToTheRightNode.contains(leftNode));
    }

    /*
    @Test
    public void testGetEdgesToNode() {
        System.out.println("getEdgesToNode");
        Node destination = null;
        Node instance = null;
        Set<Edge> expResult = null;
        Set<Edge> result = instance.getEdgesToNode(destination);
        assertEquals(expResult, result);
        
    }

     */
}
