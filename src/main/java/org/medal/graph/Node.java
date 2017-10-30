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
 *
 * @author skrymets
 */
public interface Node<I, NP, EP, N extends Node<I, NP, EP, N, E>, E extends Edge<I, NP, EP, N, E>> extends DataObject<I, NP> {

    // <E extends Edge<I, ?>>
    /**
     * Connects another node to this node with new undirected edge. A node that is being
     * connected is placed to the right side. The node to which the new is attached is
     * placed on the left side.
     *
     * @param otherNode a node to be connected
     *
     * @return new undirected edge
     *
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    E connect(N otherNode);

    E connectNodeFromLeft(N leftNode);

    E connectNodeFromRight(N rightNode);

    Collection<E> getEdges();

    //TODO: Decide whether it should be a List instead of a Set. Should we consider completely identical edges as alternatives?
    Set<E> getEdgesToNode(N destination);

    Graph<I, NP, EP, N, E> getGraph();

    /**
     * Returns a collection of the node's incoming edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Direction
     */
    Collection<E> getIncomingEdges();

    /**
     * Returns a collection of the node's incoming edges.
     *
     * @param includeUndirected Should the undirected edges be considered as incoming
     *                          either?
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Direction
     */
    Collection<E> getIncomingEdges(boolean includeUndirected);

    Set<N> getLinkedNodes();

    /**
     * Returns a collection of the node's outgoing edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Direction
     */
    Collection<E> getOutgoingEdges();

    /**
     * Returns a collection of the node's outgoing edges.
     *
     * @param includeUndirected Should the undirected edges be considered as outgoing
     *                          either?
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Direction
     */
    Collection<E> getOutgoingEdges(boolean includeUndirected);

}
