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

import java.util.Optional;

public interface Edge<N extends Node<N, E>, E extends Edge<N, E>> {

    /**
     * Returns a graph instance which this edge belongs to
     *
     * @return a graph instance, never <code>null</code>
     */
    Graph<N, E> graph();

    /**
     * Returns a node that is linked to the specified {@code node} by this edge.
     *
     * @param node a node which opposite counterparty we're looking for
     * @return a node instance on the other side of this edge if the
     * specified {@code node} belongs to this edge, otherwise - {@code null}
     */
    Optional<N> opposite(N node);

    /**
     * Return a node that resides in left position of this edge.
     *
     * @return left node, never not <code>null</code>
     */
    N left();

    /**
     * Return a node that resides in right position of this edge.
     *
     * @return right node, never not <code>null</code>
     */
    N right();

    /**
     * Collapses nodes that are linked by this edge into one node by
     * executing a collapse function on them.
     *
     * @return a collapsed node
     */
    N collapse();

    /**
     * "Cuts" this edge onto two parts and inserts a new node in-between. After this
     * operation the edge's left and right nodes are not linked to it anymore. The edge
     * is not referenced by a graph object as well.
     *
     * @return <code>Split</code> object that holds references to both parts of the
     * divided edge, and a payload of the original edge, if any.
     */
    Split<N, E> insertMiddleNode();

    /**
     * "Cuts" this edge onto two parts and inserts a given node in-between. After this
     * operation the edge's left and right nodes are not linked to it anymore, while
     * the edge still references them. The edge is not referenced by a graph object as
     * well.
     *
     * @param middleNode a node to be inserted in-between
     * @return <code>Split</code> object that holds references to both parts of the
     * divided edge, and a payload of the original edge, if any.
     * @throws NullPointerException if <code>middleNode</code> is <code>null</code>.
     */
    Split<N, E> insertMiddleNode(N middleNode);

    /**
     * Checks whether this edge is adjacent with {@code other} edge (sharing same node)
     *
     * @param other an edge which adjacency must be checked
     * @return {@code true} if this edge is adjacent with {@code other} edge, {@code false} otherwise
     */
    boolean isAdjacent(E other);

    /**
     * Checks whether this edge is incident with {@code node} (the node is connected to this edge)
     *
     * @param node a node which incidence must be checked
     * @return {@code true} if this edge is adjacent with {@code node}, {@code false} otherwise
     */
    boolean isIncident(N node);

    /**
     * Checks whether this edge joins a node to itself.
     *
     * @return {@code true} if this edge's ends are the same node, {@code false} otherwise.
     */
    boolean isLoop();

    interface Split<N extends Node<N, E>, E extends Edge<N, E>> {

        E leftEdge();

        E rightEdge();

    }

}
