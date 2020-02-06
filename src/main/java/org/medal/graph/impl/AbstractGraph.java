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

import org.medal.graph.*;
import org.medal.graph.Edge.Link;

import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public abstract class AbstractGraph<I, NP, EP, N extends AbstractNode<I, NP, EP, N, E>, E extends AbstractEdge<I, NP, EP, N, E>> implements Graph<I, NP, EP, N, E> {

    protected final Set<N> nodes = new HashSet<>();

    protected final Set<E> edges = new HashSet<>();

    public AbstractGraph() {
    }

    @Override
    public N createNode(NP payload) {
        N node = getNodeFactory().createNode();
        node.setId(getIdProvider().createId());
        node.setData(payload);
        nodes.add(node);
        return node;
    }

    @Override
    public N createNode() {
        return this.createNode(null);
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
        Set<N> newNodes = new HashSet<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                newNodes.add(createNode());
            }
        }
        return newNodes;
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
     * @return a newly created edge, or <code>UNDEFINED</code> if either left or
     * right node (or both) is <code>null</code>
     */
    @Override
    public E connectNodes(N left, N right, Link direction) {
        return connectNodes0(left, right, direction);
    }

    private E connectNodes0(N left, N right, Link direction) {

        requireNonNull(left);
        requireNonNull(right);

        if (left.getGraph() != right.getGraph()) {
            throw new IllegalArgumentException("Nodes can not belong to different graphs");
        }

        E edge = getEdgeFactory().createEdge(left, right, (direction == null) ? Link.UNDIRECTED : direction);
        edge.setId(getIdProvider().createId());

        registerEdge(edge);

        return edge;
    }

    @Override
    public E connectNodes(N left, N right) {
        return connectNodes(left, right, Link.UNDIRECTED);
    }

    @Override
    public Collection<E> connectNodes(Set<N> nodes) throws NotSameGraphException {
        requireSameGraph(nodes);
        final LinkedList<N> safeNodes = new LinkedList<N>(ofNullable(nodes).orElse(emptySet()));

        final List<E> edges = new ArrayList<>();
        while (!safeNodes.isEmpty()) {
            final N top = safeNodes.poll();
            edges.addAll(safeNodes.stream().map(n -> connectNodes0(top, n, Link.UNDIRECTED)).collect(toList()));
        }

        return edges;
    }

    private void requireSameGraph(Set<N> nodes) {
        final Set<N> nodeSet = ofNullable(nodes).orElse(emptySet());
        if (nodeSet.stream().filter(n -> n.getGraph() != this).count() > 0) {
            throw new NotSameGraphException("All the nodes must belong to this graphs.");
        }
    }

    @Override
    public void breakEdge(E edge) {
        if (edge == null || !edges.contains(edge)) {
            return;
        }

        edges.remove(edge);
    }

    boolean registerEdge(E edge) {
        return edges.add(edge);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("graph G {\n");

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

    protected abstract NodeFactory<I, NP, EP, N, E> getNodeFactory();

    protected abstract EdgeFactory<I, NP, EP, N, E> getEdgeFactory();

    protected abstract DataObject.IDProvider<I> getIdProvider();
}
