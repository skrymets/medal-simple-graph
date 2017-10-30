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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.medal.graph.Graph;
import org.medal.graph.Edge;
import org.medal.graph.Edge.Link;
import org.medal.graph.Node;

public abstract class AbstractNode<I, D> extends AbstractDataObject<I, D> implements Node<I, D> {

    private final Graph<I, D> graph;

    public AbstractNode(Graph<I, D> graph) {
        Objects.requireNonNull(graph);
        this.graph = graph;
    }

    enum InOut {
        IN, OUT
    };

    @Override
    public Collection<Edge<I, D>> getEdges() {
        return getGraph()
                .getEdges()
                .stream()
                .filter(e -> {
                    return e.getLeft() == this || e.getRight() == this;
                })
                .collect(Collectors.toSet());
    }

    /**
     * Returns a collection of the node's incoming edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Link
     */
    @Override
    public Collection<Edge<I, D>> getIncomingEdges() {
        return getIncomingEdges(false);
    }

    /**
     * Returns a collection of the node's outgoing edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Link
     */
    @Override
    public Collection<Edge<I, D>> getOutgoingEdges() {
        return getOutgoingEdges(false);
    }

    /**
     * Returns a collection of the node's incoming edges.
     *
     * @param includeUndirected Should the undirected edges be considered as incoming
     *                          either?
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Link
     */
    @Override
    public Collection<Edge<I, D>> getIncomingEdges(boolean includeUndirected) {
        return getEdges(InOut.IN, includeUndirected);
    }

    /**
     * Returns a collection of the node's outgoing edges.
     *
     * @param includeUndirected Should the undirected edges be considered as outgoing
     *                          either?
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Link
     */
    @Override
    public Collection<Edge<I, D>> getOutgoingEdges(boolean includeUndirected) {
        return getEdges(InOut.OUT, includeUndirected);
    }

    /**
     * Returns a collection of edges by their direction towards the node.
     *
     * @param inOut             Incoming or outgoing edges filter.
     * @param includeUndirected Should the undirected edges be considered as
     *                          <code>inOut</code> either?
     *
     * @return an unmodifiable collection of edges.
     */
    private Collection<Edge<I, D>> getEdges(InOut inOut, boolean includeUndirected) {

        Collection<Edge<I, D>> edges = getEdges();
        Set<Edge<I, D>> resultSet = edges.stream()
                .filter(edge -> {
                    if (edge.getDirected() == Link.UNDIRECTED && includeUndirected) {
                        return true;
                    } else if (edge.getDirected() == Link.DIRECTED) {
                        /**
                         * The definition of directions is here:
                         * org.medal.graph.Edge.Link
                         */
                        return (inOut == InOut.IN && edge.getRight() == this)
                                || (inOut == InOut.OUT && edge.getLeft() == this);
                    } else {
                        return false;
                    }
                })
                .collect(Collector.of(LinkedHashSet<Edge<I, D>>::new, Set::add, (left, right) -> {
                    left.addAll(right);
                    return left;
                }, Collections::unmodifiableSet));
        return resultSet;
    }

    @Override
    public Graph<I, D> getGraph() {
        return graph;
    }

    @Override
    public Edge<I, D> connectNodeFromRight(Node<I, D> rightNode) {
        return getGraph().connectNodes(this, rightNode, Link.DIRECTED);
    }

    @Override
    public Edge<I, D> connectNodeFromLeft(Node<I, D> leftNode) {
        return getGraph().connectNodes(leftNode, this, Link.DIRECTED);
    }

    /**
     * Connects another node to this node with new undirected edge. A node that is being
     * connected is placed to the right side. The node to which the new is attached is
     * placed on the left side.
     *
     * @param otherNode a node to be connected
     *
     * @return new undirected edge
     *
     * @throws NullPointerException if <code>otherNode</code> is undefined
     */
    @Override
    public Edge<I, D> connect(Node<I, D> otherNode) {
        return getGraph().connectNodes(this, otherNode, Link.UNDIRECTED);
    }

    @Override
    public Set<Node<I, D>> getLinkedNodes() {
        if (getEdges().isEmpty()) {
            return Collections.emptySet();
        }

        Set<Node<I, D>> oppositeNodes = new LinkedHashSet();
        for (Edge<I, D> edge : getEdges()) {
            Node<I, D> opposite = edge.getOpposite(AbstractNode.this);
            oppositeNodes.add(opposite);
        }

        return oppositeNodes;

//        return edges.stream()
//                .map((Edge<I, D> edge) -> edge.getOpposite(Node.this))
//                // avoid multiple node copies if there are more than one linked edges
//                .distinct()
//                .collect(Collectors.toSet());
    }

    //TODO: Decide whether it should be a List instead of a Set. Should we consider completely identical edges as alternatives?
    @Override
    public Set<Edge<I, D>> getEdgesToNode(Node<I, D> destination) {
        return getEdges()
                .stream()
                .filter(e -> (e.getOpposite(AbstractNode.this).equals(destination)))
                .collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        //TODO: Consider nodes equality in the comparison, but avoid endless recursion!
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getData(), other.getData())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (getData() == null) ? "node_" + hashCode() : getData().toString();
    }

}
