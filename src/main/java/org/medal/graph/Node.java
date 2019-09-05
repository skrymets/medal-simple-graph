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
public interface Node<I, N extends Node<I, N, E>, E extends Edge<I, N, E>> extends DataObject<I> {

    /**
     * Returns a graph instance which this node belongs to
     *
     * @return a graph instance, never <code>null</code>
     */
    Graph<I, N, E> getGraph();

    /**
     * Connects another node to this node with new undirected edge. A node that is being
     * connected is placed to the right side. The node to which the new is attached is
     * placed on the left side.
     * 
     * @param node a node to be connected
     *
     * @return new undirected edge
     *
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    E connect(N node);

    /**
     * Connects another node to this node with new directed edge. A node that is being
     * connected is placed to the left side. The node to which the new is attached is
     * placed on the right side.
     *
     * @param leftNode a node to be connected
     *
     * @return new undirected edge
     *
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    E connectNodeFromLeft(N leftNode);

    /**
     * Connects another node to this node with new directed edge. A node that is being
     * connected is placed to the right side. The node to which the new is attached is
     * placed on the left side.
     *
     * @param leftNode a node to be connected
     *
     * @return new undirected edge
     *
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    E connectNodeFromRight(N rightNode);

    /**
     * Returns a collection of the node's edges, including directed and undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     */
    Collection<E> getEdges();

    /**
     * Returns a collection edges, directed and undirected, that flows from this node to a
     * <code>destination</code> node, if any
     * <p>
     * TODO: Decide whether it should be a List instead of a Set. Should we consider
     * completely identical edges as alternatives?
     * </p>
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     */
    Set<E> getEdgesToNode(N destination);

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

    /**
     * Returns a collection of nodes that are linked to this node by both: directed and
     * undirected connections.
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     */
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
