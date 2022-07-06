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

import java.util.Collection;
import java.util.Set;

/**
 * @author skrymets
 */
public interface Node<N extends Node<N, E>, E extends Edge<N, E>> {

    /**
     * Returns a graph instance which this node belongs to
     *
     * @return a graph instance, never {@code null}
     */
    Graph<N, E> graph();

    /**
     * Returns the number of edges incident on this node.
     * Self-loop edges counted twice.
     *
     * @return the number of edges incident on this node
     */
    long degree();

    /**
     * Connects another node to this node with new edge. A node that is being
     * connected is placed to the right side. The node to which {@code node} is attached is
     * placed on the left side.
     *
     * @param node a node to be connected
     * @return new edge
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    E connect(N node);

    /**
     * Returns a collection of the node's incident edges, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never {@code null}.
     */
    Collection<E> incidentEdges();

    /**
     * Returns a collection of nodes that are adjacent to this node.
     *
     * @return an unmodifiable collection of edges. May be empty, but never {@code null}.
     */
    Set<N> adjacentNodes();

    /**
     * Checks whether this node is adjacent with {@code other} node
     *
     * @param other a node which adjacency must be checked
     * @return {@code true} if this node is adjacent with {@code other} node, {@code false} otherwise
     */
    boolean isAdjacent(N other);

    /**
     * Checks whether this node is incident with {@code edge} (the node is connected to this edge)
     *
     * @param edge an edge which incidence must be checked
     * @return {@code true} if this node is incident with {@code edge}, {@code false} otherwise
     */
    boolean isIncident(E edge);

    /**
     * Isolated node has no incident edges
     *
     * @return {@code true} if this node has no incident edges, {@code false} otherwise
     */
    boolean isIsolated();

    /**
     * Pendent ("leaf") node has exactly one incident edge
     *
     * @return {@code true} if this pendent edges, {@code false} otherwise
     */
    boolean isPendent();

}
