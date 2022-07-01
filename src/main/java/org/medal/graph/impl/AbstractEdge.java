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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public abstract class AbstractEdge<N extends Node<N, E>, E extends Edge<N, E>> implements Edge<N, E> {

    protected N left;

    protected N right;

    protected boolean directed;

    private final Graph<N, E> graph;

    public AbstractEdge(Graph<N, E> graph, N left, N right, boolean directed) {

        requireNonNull(graph);
        if (left == null || right == null) {
            throw new IllegalArgumentException("Neither left or right note can be undefined.");
        }

        this.graph = graph;
        this.left = left;
        this.right = right;
        this.directed = directed;
    }

    @Override
    public N getLeft() {
        return left;
    }

    @Override
    public N getRight() {
        return right;
    }

    E setLeft(N left) {
        this.left = left;
        return (E) this;
    }

    E setRight(N right) {
        this.right = right;
        return (E) this;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public Graph<N, E> getGraph() {
        return graph;
    }

    @Override
    public E setDirected() {
        this.directed = true;
        return (E) this;
    }

    @Override
    public E setDirected(boolean directed) {
        this.directed = directed;
        return (E) this;
    }

    @Override
    public E setUndirected() {
        this.directed = false;
        return (E) this;
    }

    @Override
    public Optional<N> getOpposite(N node) {
        if (left == node) {
            return Optional.of(right);
        } else if (right == node) {
            return Optional.of(left);
        } else {
            return empty();
        }
    }

    public Collection<E> getLeftSiblingEdges() {
        return left.getEdges().stream()
                .filter(e -> e != this)
                .collect(toList());
    }

    public Collection<E> getRightSiblingEdges() {
        return right.getEdges().stream()
                .filter(e -> e != this)
                .collect(toList());
    }

    @Override
    public N collapse() {
        final N collapsedNode = graph.createNode();
        relinkEdges(getLeftSiblingEdges(), left, collapsedNode);
        relinkEdges(getRightSiblingEdges(), right, collapsedNode);

        graph.breakEdge((E) this);
        graph.deleteNodes(of(left, right));
        return collapsedNode;
    }

    private void relinkEdges(Collection<E> edges, N oldTarget, N newTarget) {
        edges.stream().forEach(e -> {
            // Find an opposite node for this edge's left/right node
            // Note: this doesn't necessary mean that a left/right node in this
            // ( collapse method's target ) edge is also the left/right node
            // in the neighborEdge
            final N oppositeNode = e.getOpposite(oldTarget).get();
            if (e.getLeft() == oppositeNode) {
                ((AbstractEdge<N, E>) e).setRight(newTarget);
            } else {
                ((AbstractEdge<N, E>) e).setLeft(newTarget);
            }
        });
    }

    @Override
    public Collection<E> duplicate(int copies) {
        if (copies < 1) {
            return emptyList();
        }

        List<E> clones = range(0, copies).mapToObj(i -> duplicate()).collect(toCollection(() -> new ArrayList<>(copies)));
        return clones;
    }

    @Override
    public E duplicate() {
        E edge = getGraph().connectNodes(left, right, directed);
        return edge;
    }

    @Override
    public Split<N, E> insertMiddleNode(N middleNode) {
        if (middleNode == null) {
            throw new NullPointerException("Can not insert an undefined node.");
        }

        getGraph().breakEdge((E) this);

        E leftEdge = getGraph().connectNodes(left, middleNode, directed);

        E rightEdge = getGraph().connectNodes(middleNode, right, directed);

        Split<N, E> split = new SplitImpl(leftEdge, rightEdge);

        return split;
    }

    @Override
    public String toString() {
        return left.toString() + " -" + (directed ? '>' : '-') + ' ' + right.toString();
    }

    public static class SplitImpl<N extends Node<N, E>, E extends Edge<N, E>> implements Split<N, E> {

        private final E leftEdge;

        private final E rightEdge;

        private SplitImpl(E leftEdge, E rightEdge) {
            requireNonNull(leftEdge);
            requireNonNull(rightEdge);

            this.leftEdge = leftEdge;
            this.rightEdge = rightEdge;
        }

        @Override
        public E getLeftEdge() {
            return leftEdge;
        }

        @Override
        public E getRightEdge() {
            return rightEdge;
        }

    }
}
