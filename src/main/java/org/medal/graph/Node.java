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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.medal.graph.Edge.Direction;

/**
 *
 * @author Lot
 * @par payload type
 */
public class Node<P> extends DataObject<P> {

    public static final Node UNDEFINED = new Node(Graph.UNDEFINED);

    private final Graph graph;

    //TODO: Decide whether it should be a List instead of a Set. Should we consider completely identical edges as alternatives?
    private final Collection<Edge> edges = new LinkedHashSet<>();

    Node(Graph graph) {
        super();
        Objects.requireNonNull(graph);
        this.graph = graph;
    }

    enum InOut {
        IN, OUT
    };

    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    /**
     * Returns a collection of the node's incoming edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Direction
     */
    public Collection<Edge> getIncomingEdges() {
        return getIncomingEdges(false);
    }

    /**
     * Returns a collection of the node's outgoing edges, NOT including undirected, if any
     *
     * @return an unmodifiable collection of edges. May be empty, but never
     *         <code>null</code>.
     *
     * @see org.medal.graph.Edge.Direction
     */
    public Collection<Edge> getOutgoingEdges() {
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
     * @see org.medal.graph.Edge.Direction
     */
    public Collection<Edge> getIncomingEdges(boolean includeUndirected) {
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
     * @see org.medal.graph.Edge.Direction
     */
    public Collection<Edge> getOutgoingEdges(boolean includeUndirected) {
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
    private Collection<Edge> getEdges(InOut inOut, boolean includeUndirected) {

        return edges.stream()
                .filter(edge -> {
                    if (edge.getDirection() == Direction.UNDIRECT && includeUndirected) {
                        return true;
                    } else if (edge.getDirection() == Direction.DIRECT) {
                        /**
                         * The definition of directions is here:
                         * org.medal.graph.Edge.Direction
                         */
                        return (inOut == InOut.IN && edge.getRight() == this)
                                || (inOut == InOut.OUT && edge.getLeft() == this);
                    } else {
                        return false;
                    }
                })
                .collect(Collector.of(LinkedHashSet<Edge>::new, Set::add, (left, right) -> {
                    left.addAll(right);
                    return left;
                }, Collections::unmodifiableSet));
    }

    public Graph getGraph() {
        return graph;
    }

    public Edge connectNodeFromRight(Node rightNode) {
        return Graph.connectNodes(this, rightNode, Direction.DIRECT);
    }

    public Edge connectNodeFromLeft(Node leftNode) {
        return Graph.connectNodes(leftNode, this, Direction.DIRECT);
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
    public Edge connect(Node otherNode) {
        return Graph.connectNodes(this, otherNode, Direction.UNDIRECT);
    }

    boolean linkEdge(Edge e) {
        return edges.add(e);
    }

    boolean unLinkEdge(Edge e) {
        if (e == null || !edges.contains(e)) {
            return false;
        }
        return edges.remove(e);
    }

    public Set<Node> getLinkedNodes() {
        if (edges.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Node> oppositeNodes = new LinkedHashSet();
        for (Edge edge : edges) {
            Node opposite = edge.getOpposite(Node.this);
            oppositeNodes.add(opposite);
        }
        
        return oppositeNodes;
        
//        return edges.stream()
//                .map((Edge edge) -> edge.getOpposite(Node.this))
//                // avoid multiple node copies if there are more than one linked edges
//                .distinct()
//                .collect(Collectors.toSet());
    }

    //TODO: Decide whether it should be a List instead of a Set. Should we consider completely identical edges as alternatives?
    public Set<Edge> getEdgesToNode(Node destination) {
        return edges.stream()
                .filter((Edge edge) -> (edge.getOpposite(Node.this).equals(destination)))
                .collect(Collectors.toSet());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }        
        if (!Objects.equals(this.payload, other.payload)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (payload == null) ? "node_" + hashCode() : payload.toString();
    }

}
