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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.medal.graph.Edge.Link;
import org.medal.graph.impl.GraphImpl;

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
        graph = new GraphImpl();
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

        assertTrue(inEdge.getDirected() == Link.DIRECTED);
        assertTrue(outEdge.getDirected() == Link.DIRECTED);

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

    @Test
    public void testGetEdgesToNode() {

        List<Node> nodes = new ArrayList<>(graph.createNodes(3));
        assertEquals(nodes.size(), 3);

        Node nodeOne = nodes.get(0);
        Node nodeTwo = nodes.get(1);
        Node nodeThree = nodes.get(2);

        //
        //  [nodeOne] --(1)--- [nodeTwo]--(4)-\
        //      \   \__(2)____/                \
        //       \_____(3)________________ [nodeThree] 
        //
        Edge one2two1 = nodeOne.connect(nodeTwo);
        Edge one2two2 = nodeOne.connect(nodeTwo);
        Edge one2three1 = nodeOne.connect(nodeThree);

        assertNotSame(one2two1, one2two2);

        Set<Edge> one2twoX = nodeOne.getEdgesToNode(nodeTwo);
        assertNotNull(one2twoX);
        assertTrue(one2twoX.size() == 2);

        assertTrue(one2twoX.containsAll(Arrays.asList(one2two1, one2two2)));
        assertFalse(one2twoX.containsAll(Arrays.asList(one2three1)));

    }

}
