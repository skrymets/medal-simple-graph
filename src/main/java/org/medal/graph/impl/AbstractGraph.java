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
package org.medal.graph.impl;

import org.medal.graph.EdgeFactory;
import org.medal.graph.Graph;
import org.medal.graph.NodeFactory;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

public abstract class AbstractGraph<N extends AbstractNode<N, E>, E extends AbstractEdge<N, E>>
        implements Graph<N, E> {

    protected final Set<N> nodes = new HashSet<>();

    protected final Set<E> edges = new HashSet<>();

    protected AbstractGraph() {
    }

    @Override
    public N createNode() {
        N node = getNodeFactory().createNode();
        nodes.add(node);
        return node;
    }

    @Override
    public Set<N> nodes() {
        return unmodifiableSet(nodes);
    }

    @Override
    public Set<E> edges() {
        return unmodifiableSet(edges);
    }

    /**
     * Connects two nodes with a new edge, and registers the edge in the parent graph(s)
     *
     * @param source Left node
     * @param target Right node
     * @return a newly created edge
     * @throws NullPointerException if either source or target node (or both) is <code>null</code>
     */
    @Override
    public E connect(N source, N target) {

        requireNonNull(source);
        requireNonNull(target);

        if (source.graph() != target.graph() || source.graph() != this || target.graph() != this) {
            throw new IllegalArgumentException("Nodes can not belong to different graphs");
        }

        E edge = getEdgeFactory().createEdge(source, target);

        registerEdge(edge);

        return edge;
    }

    @Override
    public void deleteEdge(E edge) {
        if (edge == null || edge.graph() != this) {
            return;
        }

        edges.remove(edge);
    }

    boolean registerEdge(E edge) {
        return edges.add(edge);
    }

    @Override
    public boolean deleteNode(N node) {
        if (node == null || node.graph() != this) {
            return false;
        }

        this.edges.removeAll(node.incidentEdges());
        this.nodes.remove(node);
        return true;
    }

    protected abstract NodeFactory<N, E> getNodeFactory();

    protected abstract EdgeFactory<N, E> getEdgeFactory();

}
