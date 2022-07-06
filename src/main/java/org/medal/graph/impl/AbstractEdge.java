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
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

public abstract class AbstractEdge<N extends Node<N, E>, E extends Edge<N, E>> implements Edge<N, E> {

    protected N left;

    protected N right;

    private final Graph<N, E> graph;

    protected AbstractEdge(final Graph<N, E> graph, final N left, final N right) {

        requireNonNull(graph);
        if (left == null || right == null) {
            throw new IllegalArgumentException("Neither left or right note can be undefined.");
        }

        this.graph = graph;
        this.left = left;
        this.right = right;
    }

    @Override
    public N left() {
        return left;
    }

    @Override
    public N right() {
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
    public Graph<N, E> graph() {
        return graph;
    }

    @Override
    public Optional<N> opposite(N node) {
        if (left == node) {
            return of(right);
        } else if (right == node) {
            return of(left);
        } else {
            return empty();
        }
    }

    public Collection<E> getLeftSiblingEdges() {
        return left.incidentEdges().stream()
                .filter(e -> e != this)
                .collect(toList());
    }

    public Collection<E> getRightSiblingEdges() {
        return right.incidentEdges().stream()
                .filter(e -> e != this)
                .collect(toList());
    }

    @Override
    public N collapse() {
        final N collapsedNode = graph.createNode();
        relinkEdges(getLeftSiblingEdges(), left, collapsedNode);
        relinkEdges(getRightSiblingEdges(), right, collapsedNode);

        graph.deleteEdge((E) this);
        graph.deleteNode(left);
        graph.deleteNode(right);
        return collapsedNode;
    }

    private void relinkEdges(Collection<E> edges, N oldTarget, N newTarget) {
        edges.stream().forEach(e -> {
            // Find an opposite node for this edge's left/right node
            // Note: this doesn't necessary mean that a left/right node in this
            // ( collapse method's target ) edge is also the left/right node
            // in the neighborEdge
            final N oppositeNode = e.opposite(oldTarget).get();
            if (e.left() == oppositeNode) {
                ((AbstractEdge<N, E>) e).setRight(newTarget);
            } else {
                ((AbstractEdge<N, E>) e).setLeft(newTarget);
            }
        });
    }

    @Override
    public Split<N, E> insertMiddleNode() {
        final N middleNode = graph().createNode();
        return insertMiddleNode(middleNode);
    }

    @Override
    public Split<N, E> insertMiddleNode(N middleNode) {
        if (middleNode == null) {
            throw new NullPointerException("Can not insert an undefined node.");
        }

        graph().deleteEdge((E) this);

        E leftEdge = graph().connect(left, middleNode);

        E rightEdge = graph().connect(middleNode, right);

        Split<N, E> split = new SplitImpl(leftEdge, rightEdge);

        return split;
    }

    @Override
    public boolean isAdjacent(E other) {
        requireNonNull(other);
        if (graph != other.graph()) throw new IllegalArgumentException();

        return other.isIncident(left) || other.isIncident(right);
    }

    @Override
    public boolean isIncident(N node) {
        requireNonNull(node);
        if (graph != node.graph()) throw new IllegalArgumentException();

        return left == node || right == node;
    }

    @Override
    public boolean isLoop() {
        return left == right;
    }

    static class SplitImpl<N extends Node<N, E>, E extends Edge<N, E>> implements Split<N, E> {

        private final E leftEdge;

        private final E rightEdge;

        private SplitImpl(E leftEdge, E rightEdge) {
            requireNonNull(leftEdge);
            requireNonNull(rightEdge);

            this.leftEdge = leftEdge;
            this.rightEdge = rightEdge;
        }

        @Override
        public E leftEdge() {
            return leftEdge;
        }

        @Override
        public E rightEdge() {
            return rightEdge;
        }

    }
}
