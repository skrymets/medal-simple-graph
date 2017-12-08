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
import java.util.List;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

/**
 *
 * @author skrymets
 */
public class GraphUtilsTest {

    public GraphUtilsTest() {
    }

    @Test(expected = NullPointerException.class)
    public void testNodesAreAdjacent() {
        GraphImpl graph = new GraphImpl();
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(4));

        // [n1] -----(e1to2)----- [n2] -----(e2to3)----- [n3] 
        //                        [n4] _____(e3to4)_______|
        Node n1 = nodes.get(0);
        Node n2 = nodes.get(1);
        Node n3 = nodes.get(2);
        Node n4 = nodes.get(3);

        Edge e1to2 = n1.connect(n2);
        Edge e2to3 = n2.connect(n3);
        Edge e3to4 = n3.connect(n4);

        assertTrue(GraphUtils.nodesAreAdjacent(n1, n2));
        assertTrue(GraphUtils.nodesAreAdjacent(n2, n3));
        
        assertFalse(GraphUtils.nodesAreAdjacent(n1, n4));
        assertFalse(GraphUtils.nodesAreAdjacent(n2, n4));

        // Do not allow nulls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        boolean takeFirst = new Random().nextBoolean();
        // One of them will be null
        GraphUtils.nodesAreAdjacent(takeFirst ? n1 : null, takeFirst ? null : n2);
        fail("Should not process nulls. Must throw NullPointerException.");
    }

    @Test(expected = NullPointerException.class)
    public void testEdgesAreAdjacent() {
        // Not ALL four vertices should be different
        // ... Case 1 
        // ... -----(edge1)------[edge1.right:edge2.left] -----(edge2)------ ...
        // ... 
        // ... Case 2
        // ... -----(edge1)------\
        // ...                   [edge1.right == edge2.right]
        // ... -----(edge2)------/
        // ... 
        // ... Case 3
        // ...                          /-----(edge1)------
        // ... [edge1.left == edge2.left]
        // ...                          \-----(edge2)------

        GraphImpl graph = new GraphImpl();
        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(4));

        // =============================================================================== 
        // Case 1 
        // ===============================================================================
        // ... ---(edge0to1)---- [node1 == edge1.right:edge2.left] ---(edge1to2)---- ...
        // ... 
        Node node0 = nodes.get(0);
        Node node1 = nodes.get(1);
        Node node2 = nodes.get(2);
        Edge edge0to1 = node0.connect(node1);
        Edge edge1to2 = node1.connect(node2);

        assertTrue(GraphUtils.edgesAreAdjacent(edge0to1, edge1to2));
        assertTrue(GraphUtils.edgesAreAdjacent(edge1to2, edge0to1));

        // ... 
        // ... ---(edge0to1)---- [node1] ---....--- [node2] ---(edge2to3)---- 
        // ... 
        Node node3 = nodes.get(3);
        Edge edge2to3 = node2.connect(node3);
        assertFalse(GraphUtils.edgesAreAdjacent(edge0to1, edge2to3));

        // =============================================================================== 
        // Case 2 and Case 3
        // ===============================================================================
        // ... 
        // ... [node0] ---(edge0to1)---- [node1] ---....--- [node2] ---(edge2to3)---- [node3]
        // ...    \___________________________(edge0to3)________________________________|
        // ... 
        Edge edge0to3 = node0.connect(node3);
        assertTrue(GraphUtils.edgesAreAdjacent(edge0to1, edge0to3));
        assertTrue(GraphUtils.edgesAreAdjacent(edge0to3, edge2to3));

        // Do not allow nulls ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        boolean takeFirst = new Random().nextBoolean();
        // One of them will be null
        GraphUtils.edgesAreAdjacent(takeFirst ? edge1to2 : null, takeFirst ? null : edge2to3);
        fail("Should not process nulls. Must throw NullPointerException.");
    }

}
