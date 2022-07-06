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

import java.util.Set;

public interface Graph<N extends Node<N, E>, E extends Edge<N, E>> {

    /**
     * Create a new graph node.
     *
     * @return a new node.
     */
    N createNode();

    /**
     * Deletes nodes from this graph. Also deletes all connected edges
     *
     * @param node a node to be deleted.
     * @return {@code true} if the node {@code N} was deleted, {@code false} otherwise.
     */
    boolean deleteNode(N node);

    /**
     * Creates a new edge between two nodes.<br/>
     * Connects {@code left} and {@code right} nodes with a new
     * {@code edge} with a unique ID.
     *
     * @param left  an end node of the relation
     * @param right an end node of the relation
     * @return a new <code>Edge</code> instance.
     * @throws NullPointerException if {@code left} or {@code right} node is
     *                              undefined - {@code null}.
     */
    E connect(N left, N right);

    /**
     * Returns an unmodifiable set of edges in this graph.
     *
     * @return a set of edges. Never {@code null}
     */
    Set<E> edges();

    /**
     * Returns an unmodifiable set of nodes in this graph.
     *
     * @return a set of nodes. Never {@code null}
     */
    Set<N> nodes();

    /**
     * Removes edge from this graph. Both left and right nodes do not refer this edge
     * anymore. <br/>
     * If {@code edge} is {@code null} - does nothing.
     *
     * @param edge edge to be removed.
     */
    void deleteEdge(E edge);

}
