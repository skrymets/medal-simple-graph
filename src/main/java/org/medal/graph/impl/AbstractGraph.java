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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import org.medal.graph.EdgeFactory;
import org.medal.graph.Edge.Link;
import org.medal.graph.IDProvider;
import org.medal.graph.NodeFactory;
import org.medal.graph.Node;
import org.medal.graph.Graph;
import org.medal.graph.Edge;

public abstract class AbstractGraph<I, D> implements Graph<I, D> {

    private final Set<Node<I, D>> nodes = new HashSet<>();

    private final Set<Edge<I, D>> edges = new LinkedHashSet<>();

    @Override
    public Node<I, D> createNode(D payload) {
        Node<I, D> node = getNodeFactory().createNode();
        node.setId(getIdProvider().createId());
        node.setData(payload);
        nodes.add(node);
        return node;
    }

    /**
     * Creates several new nodes that are not connected at this moment.
     *
     * @param count a number of nodes to create
     *
     * @return a list of nodes that were created or an empty list, if <code>count</code>
     *         is less or equal to zero.
     */
    @Override
    public Collection<Node<I, D>> createNodes(int count) {
        Collection<Node<I, D>> newNodes = new LinkedList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                newNodes.add(createNode());
            }
        }
        return newNodes;
    }

    @Override
    public Node<I, D> createNode() {
        return this.createNode(null);
    }

    @Override
    public Set<Node<I, D>> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    @Override
    public Set<Edge<I, D>> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    /**
     * Connects two nodes with a new edge, and registers the edge in the parent graph(s)
     *
     * @param left  Left node
     * @param right Right node
     *
     * @return a newly created edge, or <code>UNDEFINED</code> if either left or
     *         right node (or both) is <code>null</code>
     */
    @Override
    public Edge<I, D> connectNodes(Node<I, D> left, Node<I, D> right, Link direction) {

        Objects.requireNonNull(left);
        Objects.requireNonNull(right);

        if (left.getGraph() != right.getGraph()) {
            throw new IllegalArgumentException("Nodes can not belong to different graphs");
        }

        Edge<I, D> edge = getEdgeFactory().createEdge(left, right, (direction == null) ? Link.UNDIRECTED : direction);
        edge.setId(getIdProvider().createId());

        registerEdge(edge);

        return edge;
    }

    @Override
    public Edge<I, D> connectNodes(Node<I, D> left, Node<I, D> right) {
        return connectNodes(left, right, Link.UNDIRECTED);
    }

    @Override
    public void breakEdge(Edge<I, D> edge) {
        if (edge == null || !edges.contains(edge)) {
            return;
        }

        edges.remove(edge);
    }

    boolean registerEdge(Edge<I, D> e) {
        return edges.add(e);
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

    protected abstract NodeFactory<I, D> getNodeFactory();

    protected abstract EdgeFactory<I, D> getEdgeFactory();

    protected abstract IDProvider<I> getIdProvider();
}
