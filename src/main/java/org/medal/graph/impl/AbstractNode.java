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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.medal.graph.impl.AbstractNode.InOut.IN;
import static org.medal.graph.impl.AbstractNode.InOut.OUT;

public abstract class AbstractNode<N extends Node<N, E>, E extends Edge<N, E>> implements Node<N, E> {

    private final Graph<N, E> graph;

    protected AbstractNode(final Graph<N, E> graph) {
        requireNonNull(graph);
        this.graph = graph;
    }

    enum InOut {
        IN, OUT
    }

    @Override
    public Collection<E> incidentEdges() {
        return graph()
                .edges()
                .stream()
                .map(edge -> (E) edge)
                .filter(e -> e.left() == this || e.right() == this)
                .collect(collectingAndThen(
                        toSet(),
                        Collections::unmodifiableSet
                ));
    }

    @Override
    public Collection<E> incomingEdges() {
        return incomingEdges(false);
    }

    @Override
    public Collection<E> outgoingEdges() {
        return outgoingEdges(false);
    }

    @Override
    public Collection<E> incomingEdges(boolean includeUndirected) {
        return incidentEdges(IN, includeUndirected);
    }

    @Override
    public Collection<E> outgoingEdges(boolean includeUndirected) {
        return incidentEdges(OUT, includeUndirected);
    }

    @Override
    public boolean isAdjacent(N other) {
        requireNonNull(other);
        if (this.graph != other.graph())
            throw new IllegalArgumentException();

        return incidentEdges().stream()
                .map(edge -> edge.opposite((N) this).orElse(null))
                .filter(Objects::nonNull)
                .anyMatch(node -> node == other);
    }

    @Override
    public boolean isIncident(E edge) {
        requireNonNull(edge);
        if (this.graph != edge.graph())
            throw new IllegalArgumentException();

        return edge.left() == this || edge.right() == this;
    }

    @Override
    public boolean isIsolated() {
        return degree() == 0;
    }

    @Override
    public boolean isPendent() {
        return degree() == 1;
    }

    /**
     * Returns a collection of edges by their direction towards the node.
     *
     * @param inOut             Incoming or outgoing edges filter.
     * @param includeUndirected Should the undirected edges be considered as
     *                          <code>inOut</code> either?
     * @return an unmodifiable collection of edges.
     */
    private Collection<E> incidentEdges(InOut inOut, boolean includeUndirected) {

        return incidentEdges().stream()
                .filter(edge -> {
                    if (!edge.isDirected() && includeUndirected) {
                        return true;
                    } else if (edge.isDirected()) {
                        /*
                         * The definition of directions is here:
                         * org.medal.graph.Edge.Link
                         */
                        return (inOut == IN && edge.right() == this)
                                || (inOut == OUT && edge.left() == this);
                    } else {
                        return false;
                    }
                }).collect(collectingAndThen(
                        toSet(),
                        Collections::unmodifiableSet
                ));
    }

    @Override
    public Graph<N, E> graph() {
        return graph;
    }

    @Override
    public long degree() {
        long degree = 0;

        for (E e : incidentEdges()) degree += e.isLoop() ? 2 : 1;
        return degree;
    }

    @Override
    public long inDegree() {
        return incidentEdges(IN, false).size();
    }

    @Override
    public long outDegree() {
        return incidentEdges(OUT, false).size();
    }

    @Override
    public E connectTarget(N targetNode) {
        return graph().connect((N) this, targetNode, true);
    }

    @Override
    public E connectSource(N sourceNode) {
        return graph().connect(sourceNode, (N) this, true);
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
        return graph().connect((N) this, otherNode, false);
    }

    @Override
    public Set<N> adjacentNodes() {
        return incidentEdges().stream()
                .map((edge) -> edge.opposite((N) AbstractNode.this)
                        .orElseThrow(() -> new IllegalArgumentException("This node does not belong to an edge")))
                .collect(collectingAndThen(
                        toSet(),
                        Collections::unmodifiableSet
                ));
    }

}
