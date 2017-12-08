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

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import java.util.HashSet;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author skrymets
 */
public class GraphUtils {

    /**
     * Two nodes of a graph are called adjacent if they share a common edge (regardless of
     * whether it is directional or not.)
     *
     * @return <code>true</code> if both <code>node1</code> and <code>node2</code> are
     *         adjacent.
     *
     * @throws NullPointerException if <code>node1</code> or <code>node2</code> is
     *                              <code>null</code>.
     */
    public static boolean nodesAreAdjacent(Node node1, Node node2) {
        requireNonNull(node1);
        requireNonNull(node2);

        return node1.getEdgesToNode(node2).size() > 0;

    }

    /**
     * Two edges of a graph are called adjacent if they share a common vertex
     *
     * @return <code>true</code> if both <code>edge1</code> and <code>edge2</code> are
     *         adjacent.
     *
     * @throws NullPointerException if <code>edge1</code> or <code>edge2</code> is
     *                              <code>null</code>.
     */
    public static boolean edgesAreAdjacent(Edge edge1, Edge edge2) {
        requireNonNull(edge1);
        requireNonNull(edge2);

        if (edge1 == edge2) {
            return false;
        }

        return (new HashSet<>(
                asList(edge1.getLeft(), edge1.getRight(),
                        edge2.getLeft(), edge2.getRight()
                ))).size() < 4;
    }

}
