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

import org.medal.graph.Edge.Link;
import org.medal.graph.events.GraphEventsSubscriber;

import java.util.Set;

public interface Graph<I, N extends Node<I, N, E>, E extends Edge<I, N, E>> {

    /**
     * Create a new graph node with a unique ID.
     *
     * @param payload node's payload. May be <code>null</code>
     * @return a new node.
     * @see org.medal.graph.IDProvider
     */
    N createNode(Object payload);

    /**
     * Create a new graph node with a unique ID.
     *
     * @return a new node.
     * @see org.medal.graph.IDProvider
     */
    N createNode();

    /**
     * Creates several new nodes that are not connected among each other at this moment.
     *
     * @param count a number of nodes to create
     * @return a list of nodes that were created or an empty list, if <code>count</code>
     * is less or equal to zero.
     */
    Set<N> createNodes(int count);

    /**
     * Creates a new link between two nodes.<br/>
     * Connects <code>left</code> and <code>right</code> nodes with a new
     * <code>Edge</code> with a unique ID.
     *
     * @param left      node to be placed at the left side of the relation
     * @param right     node to be placed at the right side of the relation
     * @param direction sets new edge to be whether <code>DIRECTED</code> or
     *                  <code>UNDIRECTED</code>.
     * @return a new <code>Edge</code> instance.
     * @throws NullPointerException if <code>left</code> or <code>right</code> node is
     *                              undefined - <code>null</code>.
     * @see DataObject.IDProvider
     * @see org.medal.graph.Edge.Link#UNDIRECTED
     */
    E connectNodes(N left, N right, Link direction);

    /**
     * Creates an <code>UNDIRECTED</code> link between two nodes.<br/>
     * Connects <code>left</code> and <code>right</code> nodes with a new
     * <code>Edge</code> with a unique ID.
     *
     * @param left  node to be placed at the left side of the relation
     * @param right node to be placed at the right side of the relation
     * @return a new <code>Edge</code> instance.
     * @throws NullPointerException if <code>left</code> or <code>right</code> node is
     *                              undefined - <code>null</code>.
     * @see DataObject.IDProvider
     * @see org.medal.graph.Edge.Link#UNDIRECTED
     */
    E connectNodes(N left, N right);

    /**
     * Returns an unmodifiable set of edges in this graph.
     *
     * @return a set of edges. Never <code>null</code>
     */
    Set<E> getEdges();

    /**
     * Returns an unmodifiable set of nodes in this graph.
     *
     * @return a set of nodes. Never <code>null</code>
     */
    Set<N> getNodes();

    /**
     * Removes edge from this graph. Both left and right nodes do not refer this edge
     * anymore. <br/>
     * If <code>edge</code> is <code>null</code> - does nothing.
     *
     * @param edge edge to be removed.
     */
    void breakEdge(E edge);

    <S extends GraphEventsSubscriber<I, N, E>> void addSubscriber(S subscriber);

    <S extends GraphEventsSubscriber<I, N, E>> void removeSubscriber(S subscriber);

    /**
     * Deletes nodes from this graph. Also deletes all connected edges
     *
     * @param nodes a collection  of nodes to be deleted.
     */
    void deleteNodes(Collection<N> nodes);

}
