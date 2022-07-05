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

import org.medal.graph.Edge;
import org.medal.graph.Graph;
import org.medal.graph.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public abstract class AbstractNode<N extends Node<N, E>, E extends Edge<N, E>> implements Node<N, E> {

    private final Graph<N, E> graph;

    public AbstractNode(Graph<N, E> graph) {
        Objects.requireNonNull(graph);
        this.graph = graph;
    }

    enum InOut {
        IN, OUT
    }

    @Override
    public Collection<E> getEdges() {
        return getGraph()
                .getEdges()
                .stream()
                .map(edge -> (E) edge)
                .filter(e -> e.getLeft() == this || e.getRight() == this)
                .collect(collectingAndThen(
                        toSet(),
                        Collections::unmodifiableSet
                ));
    }

    @Override
    public Collection<E> getIncomingEdges() {
        return getIncomingEdges(false);
    }

    @Override
    public Collection<E> getOutgoingEdges() {
        return getOutgoingEdges(false);
    }

    @Override
    public Collection<E> getIncomingEdges(boolean includeUndirected) {
        return getEdges(InOut.IN, includeUndirected);
    }

    @Override
    public Collection<E> getOutgoingEdges(boolean includeUndirected) {
        return getEdges(InOut.OUT, includeUndirected);
    }

    /**
     * Returns a collection of edges by their direction towards the node.
     *
     * @param inOut             Incoming or outgoing edges filter.
     * @param includeUndirected Should the undirected edges be considered as
     *                          <code>inOut</code> either?
     * @return an unmodifiable collection of edges.
     */
    private Collection<E> getEdges(InOut inOut, boolean includeUndirected) {

        Collection<E> edges = getEdges();
        return edges.stream()
                .filter(edge -> {
                    if (edge.isDirected() == false && includeUndirected) {
                        return true;
                    } else if (edge.isDirected()) {
                        /*
                         * The definition of directions is here:
                         * org.medal.graph.Edge.Link
                         */
                        return (inOut == InOut.IN && edge.getRight() == this)
                                || (inOut == InOut.OUT && edge.getLeft() == this);
                    } else {
                        return false;
                    }
                })
                .collect(collectingAndThen(
                        toSet(),
                        Collections::unmodifiableSet
                ));
    }

    @Override
    public Graph<N, E> getGraph() {
        return graph;
    }

    @Override
    public E connectNodeFromRight(N rightNode) {
        return getGraph().connectNodes((N) this, rightNode, true);
    }

    @Override
    public E connectNodeFromLeft(N leftNode) {
        return getGraph().connectNodes(leftNode, (N) this, true);
    }

    /**
     * Connects another node to this node with new undirected edge. A node that is being
     * connected is placed to the right side. The node to which the new is attached is
     * placed on the left side.
     *
     * @param otherNode a node to be connected
     * @return new undirected edge
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    @Override
    public E connect(N otherNode) {
        return getGraph().connectNodes((N) this, otherNode, false);
    }

    @Override
    public Set<N> getLinkedNodes() {
        return getEdges().stream()
                .map((edge) -> edge.getOpposite((N) AbstractNode.this)
                        .orElseThrow(() -> new IllegalArgumentException("This node does not belong to an edge")))
                .collect(collectingAndThen(
                        toSet(),
                        Collections::unmodifiableSet
                ));
    }

}
