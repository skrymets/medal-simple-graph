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
     * Returns the number of edges incident on this node, both: directed and undirected.
     * Self-loop edges counted twice.
     *
     * @return the number of edges incident on this node
     */
    long degree();

    /**
     * Returns the number of directed edges incident and ending on this node.
     * This node is the head of that edges.
     *
     * @return a number {@code >= 0}
     */
    long inDegree();

    /**
     * Returns the number of directed edges incident and beginning on this node.
     * This node is the tail of that edges.
     *
     * @return a number {@code >= 0}
     */
    long outDegree();

    /**
     * Connects another node to this node with new undirected edge. A node that is being
     * connected is placed to the right (target) side. The node to which {@code node} is attached is
     * placed on the left (source) side.
     *
     * @param node a node to be connected
     * @return new undirected edge
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    E connect(N node);

    /**
     * Connects another node to this node with new directed edge. A node that is being
     * connected is placed to the source (tail) side. The node to which {@code sourceNode} is attached is
     * placed on the right (head) side.
     *
     * @param sourceNode a node to be connected
     * @return new undirected edge
     * @throws NullPointerException if {@code sourceNode} is undefined
     */
    E connectSource(N sourceNode);

    /**
     * Connects another node to this node with new directed edge. A node that is being
     * connected is placed to the target (head) side. The node to which {@code targetNode} is attached is
     * placed on the left (tail) side.
     *
     * @param targetNode a node to be connected
     * @return new undirected edge
     * @throws NullPointerException if {@code targetNode} is undefined
     */
    E connectTarget(N targetNode);

    /**
     * Returns a collection of the node's incident edges, including directed and undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never {@code null}.
     */
    Collection<E> incidentEdges();

    /**
     * Returns a collection of the node's incoming edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     * {@code null}.
     */
    Collection<E> incomingEdges();

    /**
     * Returns a collection of the node's incoming edges.
     *
     * @param includeUndirected Should the undirected edges be considered as incoming
     *                          either?
     * @return an unmodifiable collection of edges. May be empty, but never
     * {@code null}.
     */
    Collection<E> incomingEdges(boolean includeUndirected);

    /**
     * Returns a collection of nodes that are adjacent to this node.
     *
     * @return an unmodifiable collection of edges. May be empty, but never {@code null}.
     */
    Set<N> adjacentNodes();

    /**
     * Returns a collection of the node's outgoing edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     * {@code null}.
     */
    Collection<E> outgoingEdges();

    /**
     * Returns a collection of the node's outgoing edges.
     *
     * @param includeUndirected Should the undirected edges be considered as outgoing
     *                          either?
     * @return an unmodifiable collection of edges. May be empty, but never
     * {@code null}.
     */
    Collection<E> outgoingEdges(boolean includeUndirected);

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
