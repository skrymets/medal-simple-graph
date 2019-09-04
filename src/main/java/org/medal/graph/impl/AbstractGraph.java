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

import org.medal.graph.Edge.Link;
import org.medal.graph.*;
import org.medal.graph.events.GraphEventsSubscriber;
import org.medal.graph.events.NodesCreatedEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public abstract class AbstractGraph<I, NP, EP, N extends AbstractNode<I, NP, EP, N, E>, E extends AbstractEdge<I, NP, EP, N, E>> implements Graph<I, NP, EP, N, E> {

    protected final Set<N> nodes = new HashSet<>();

    protected final Set<E> edges = new HashSet<>();

    protected final List<GraphEventsSubscriber<I, NP, EP, N, E>> eventSubscribers = new CopyOnWriteArrayList<>();


    public AbstractGraph() {
    }

    @Override
    public N createNode(NP payload) {
        return createNode0(payload);
    }

    private N createNode0(NP payload) {
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
            newNodes = IntStream.range(0, count).mapToObj(i -> createNode()).collect(toSet());
        }
        notifySubscribers(new NodesCreatedEvent<>(this, unmodifiableSet(newNodes)));
        return newNodes;
    }

    private <T extends GraphEvent<I, NP, EP, N, E>> void notifySubscribers(T event) {
        eventSubscribers.stream().forEach(subscriber -> subscriber.processEvent(event));
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
        StringBuilder sb = new StringBuilder("graph CodeFlow {\n");

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
    public <S extends GraphEventsSubscriber<I, NP, EP, N, E>> void addSubscriber(S subscriber) {
        eventSubscribers.add(subscriber);
    }

    @Override
    public <S extends GraphEventsSubscriber<I, NP, EP, N, E>> void removeSubscriber(S subscriber) {
        eventSubscribers.remove(subscriber);
    }

    protected abstract NodeFactory<I, NP, EP, N, E> getNodeFactory();

    protected abstract EdgeFactory<I, NP, EP, N, E> getEdgeFactory();

    protected abstract IDProvider<I> getIdProvider();
}
