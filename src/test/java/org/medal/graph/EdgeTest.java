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
import org.medal.graph.impl.EdgeImpl;
import org.medal.graph.impl.GraphImpl;
import org.medal.graph.impl.NodeImpl;

/**
 *
 * @author skrymets
 */
public class EdgeTest {

    protected GraphImpl graph;

    public EdgeTest() {
    }

    @Before
    public void prepareData() {
        graph = new GraphImpl();
        graph.getNodes();
    }

    @Test
    public void testGetGraph() {

        List<NodeImpl> nodes = new ArrayList<>(graph.createNodes(2));
        assertEquals(nodes.size(), 2);

        NodeImpl node1 = nodes.get(0);
        NodeImpl node2 = nodes.get(1);

        EdgeImpl edge = node1.connect(node2);
        assertNotNull(edge.getGraph());
        assertSame(edge.getGraph(), graph);

    }

}
