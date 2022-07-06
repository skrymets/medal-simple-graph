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
        assertNotNull(node1.incidentEdges());
        assertTrue(node1.incidentEdges().isEmpty());

        EdgeImpl connection = node1.connect(node2);

        assertFalse(node1.incidentEdges().isEmpty());
        assertFalse(node2.incidentEdges().isEmpty());

        // EdgeImpl is connected to the nodes
        assertSame(connection, node1.incidentEdges().iterator().next());
        assertSame(node1.incidentEdges().iterator().next(), node2.incidentEdges().iterator().next());

        node1.incidentEdges().add(connection);
        fail("Should throw UnsupportedOperationException");
    }

    @Test
    public void testGetGraph() {

        NodeImpl node = graph.createNode();
        assertNotNull(node.graph());
        assertSame(node.graph(), graph);

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
        leftNode.connect(middleNode);
        leftNode.connect(rightNode);
        middleNode.connect(rightNode);

        final Set<NodeImpl> linkedToTheLeftNode = leftNode.adjacentNodes();
        final Set<NodeImpl> linkedToTheRightNode = rightNode.adjacentNodes();

        assertNotNull(linkedToTheLeftNode);
        assertTrue(linkedToTheLeftNode.size() == 2);
        assertTrue(linkedToTheLeftNode.contains(middleNode) && linkedToTheLeftNode.contains(rightNode));

        assertNotNull(linkedToTheRightNode);
        assertTrue(linkedToTheRightNode.size() == 2);
        assertTrue(linkedToTheRightNode.contains(middleNode) && linkedToTheRightNode.contains(leftNode));
    }

    @Test
    public void testPendentNode() {
        final NodeImpl node1 = graph.createNode();
        assertFalse(node1.isPendent());

        final NodeImpl node2 = graph.createNode();
        node1.connect(node2);

        assertTrue(node1.isPendent());
    }

    @Test
    public void testIsolatedNode() {
        final NodeImpl node1 = graph.createNode();
        assertTrue(node1.isIsolated());

        final NodeImpl node2 = graph.createNode();
        node1.connect(node2);

        assertFalse(node1.isIsolated());
    }

    @Test
    public void testNodeIncidence() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();
        final NodeImpl node3 = graph.createNode();
        final EdgeImpl edge1to2 = node1.connect(node2);
        final EdgeImpl edge2to3 = node2.connect(node3);

        assertTrue(node1.isIncident(edge1to2));
        assertFalse(node1.isIncident(edge2to3));
    }

    @Test
    public void testAdjacentNodes() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();
        final NodeImpl node3 = graph.createNode();

        node1.connect(node2);
        node2.connect(node3);

        assertTrue(node1.isAdjacent(node2));
        assertFalse(node1.isAdjacent(node3));

        node1.connect(node3);
        assertTrue(node1.isAdjacent(node3));
    }

    @Test
    public void testNodeDegree() {
        final NodeImpl node1 = graph.createNode();
        final NodeImpl node2 = graph.createNode();
        final NodeImpl node3 = graph.createNode();
        final NodeImpl node4 = graph.createNode();

        node1.connect(node2);
        node1.connect(node3);
        node1.connect(node4);

        assertEquals(3, node1.degree());
    }

}
