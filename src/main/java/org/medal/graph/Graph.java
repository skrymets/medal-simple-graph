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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph extends DataObject {

    public static final Graph UNDEFINED = new Graph();

    private final Set<Node> nodes = new HashSet<>();

    private final Set<Edge> edges = new LinkedHashSet<>();

    public Graph() {
        super();
    }

    public Node createNode(Object payload) {
        Node node = new Node(this);
        node.setPayload(payload);
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
    public Collection<Node> createNodes(int count) {
        Collection<Node> newNodes = new LinkedList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                newNodes.add(createNode());
            }
        }
        return newNodes;
    }

    public Node createNode() {
        return this.createNode(null);
    }

    public Set<Node> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public Set<Edge> getEdges() {
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
    public static Edge connectNodes(Node left, Node right, Edge.Direction direction) {

        Objects.requireNonNull(left);
        Objects.requireNonNull(right);

        Edge edge = new Edge(left, right, (direction == null) ? Edge.Direction.UNDIRECT : direction);

        left.linkEdge(edge);
        right.linkEdge(edge);

        //TODO: Should graphs be the same? If yes - only one side may provide a reference to the parent graph
        Graph.composite(left.getGraph(), right.getGraph()).registerEdge(edge);

        return edge;
    }

    public static Edge connectNodes(Node left, Node right) {
        return connectNodes(left, right, Edge.Direction.UNDIRECT);
    }

    public void breakEdge(Edge edge) {
        if (edge == null || !edges.contains(edge)) {
            return;
        }

        edge.getLeft().unLinkEdge(edge);
        edge.getRight().unLinkEdge(edge);

        edges.remove(edge);
    }

    boolean registerEdge(Edge e) {
        return edges.add(e);
    }

    public static Graph composite(Graph... graphs) {
        return new CompositeGraph(graphs);
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

}

class CompositeGraph extends Graph {

    private final Set<Graph> composite = new HashSet<>();

    CompositeGraph(Graph... graphs) {
        composite.addAll(Arrays.asList(graphs));
    }

    @Override
    public String toString() {
        return composite
                .stream()
                .map(graph -> graph.toString())
                .collect(Collectors.toList())
                .toString();
    }

    @Override
    boolean registerEdge(Edge edge) {
        boolean result = true;
        for (Graph graph : composite) {
            result = (result && graph.registerEdge(edge));
        }
        return result;
    }

    @Override
    public void breakEdge(Edge edge) {
        for (Graph graph : composite) {
            graph.breakEdge(edge);
        }
    }

    @Override
    public Set<Edge> getEdges() {
        //TODO: Should be immutable?
        return composite.stream()
                .map(graph -> graph.getEdges())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Node> getNodes() {
        //TODO: Should be immutable?
        return composite.stream()
                .map(graph -> graph.getNodes())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Node createNode() {
        //TODO: Should we create a node in all graphs? Doesn't seem to be good idea so far.
        return Node.UNDEFINED;
    }

}
