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

public abstract class AbstractNode<N extends Node<N, E>, E extends Edge<N, E>> implements Node<N, E> {

    private final Graph<N, E> graph;

    protected AbstractNode(final Graph<N, E> graph) {
        requireNonNull(graph);
        this.graph = graph;
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

    /**
     * Connects another node to this node with new edge. A node that is being
     * connected is placed to the right side. The node to which the new is attached is
     * placed on the left side.
     *
     * @param otherNode a node to be connected
     * @return new edge
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    @Override
    public E connect(N otherNode) {
        return graph().connect((N) this, otherNode);
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
