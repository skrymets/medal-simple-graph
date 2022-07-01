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

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

public abstract class AbstractGraph<N extends AbstractNode<N, E>, E extends AbstractEdge<N, E>>
        implements Graph<N, E> {

    protected final Set<N> nodes = new HashSet<>();

    protected final Set<E> edges = new HashSet<>();

    public AbstractGraph() {
    }

    @Override
    public N createNode() {
        N node = getNodeFactory().createNode();
        nodes.add(node);
        return node;
    }

    /**
     * Creates several new nodes that are not connected at this moment.
     *
     * @param count a number of nodes to create
     * @return a list of nodes that were created or an empty list, if <code>count</code>
     * is less or equal to zero.
     */
    @Override
    public Set<N> createNodes(int count) {
        if (count < 1) return emptySet();

        return range(0, count).mapToObj(i -> createNode()).collect(toSet());
    }

    @Override
    public Set<N> getNodes() {
        return unmodifiableSet(nodes);
    }

    @Override
    public Set<E> getEdges() {
        return unmodifiableSet(edges);
    }

    /**
     * Connects two nodes with a new edge, and registers the edge in the parent graph(s)
     *
     * @param left  Left node
     * @param right Right node
     * @return a newly created edge
     * @throws NullPointerException if either left or right node (or both) is <code>null</code>
     */
    @Override
    public E connectNodes(N left, N right, boolean directed) {

        requireNonNull(left);
        requireNonNull(right);

        if (left.getGraph() != right.getGraph()) {
            throw new IllegalArgumentException("Nodes can not belong to different graphs");
        }

        E edge = getEdgeFactory().createEdge(left, right, directed);

        registerEdge(edge);

        return edge;
    }

    @Override
    public E connectNodes(N left, N right) {
        return connectNodes(left, right, false);
    }

    @Override
    public void breakEdge(E edge) {
        if (edge == null) {
            return;
        }

        edges.remove(edge);
    }

    boolean registerEdge(E edge) {
        return edges.add(edge);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("graph {\n");

        nodes.stream().forEach((node) -> {
            sb.append(node.toString()).append("\n");
        });

        edges.stream().forEach((edge) -> {
            sb.append(edge.toString()).append("\n");
        });

        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public void deleteNodes(Collection<N> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }

        // TODO: 2019-09-10 Remove only nodes that belong to this particular graph
        final Set<E> edges = nodes.stream()
                .filter(Objects::nonNull)
                .map(n -> n.getEdges())
                .flatMap(Collection::stream)
                .collect(toSet());

        this.edges.removeAll(edges);
        this.nodes.removeAll(nodes);
    }

    protected abstract NodeFactory<N, E> getNodeFactory();

    protected abstract EdgeFactory<N, E> getEdgeFactory();

}
