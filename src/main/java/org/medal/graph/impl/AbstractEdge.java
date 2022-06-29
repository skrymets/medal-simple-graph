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

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;

public abstract class AbstractEdge<N extends Node<N, E>, E extends Edge<N, E>> implements Edge<N, E> {

    protected final N left;

    protected final N right;

    protected Link link;

    private final Graph<N, E> graph;

    public AbstractEdge(
            Graph<N, E> graph,
            N left,
            N right,
            Link link) {

        requireNonNull(graph);
        if (left == null || right == null) {
            throw new IllegalArgumentException("Neither left or right note can be undefined.");
        }

        this.graph = graph;
        this.left = left;
        this.right = right;
        this.link = (link == null) ? Link.UNDIRECTED : link;
    }

    @Override
    public N getLeft() {
        return left;
    }

    @Override
    public N getRight() {
        return right;
    }

    @Override
    public Link getDirected() {
        return link;
    }

    @Override
    public Graph<N, E> getGraph() {
        return graph;
    }

    @Override
    public E setDirected(Link direction) {
        this.link = (direction == null) ? Link.UNDIRECTED : direction;
        return (E) this;
    }

    @Override
    public N getOpposite(N node) {
        if (left == node) {
            return right;
        } else if (right == node) {
            return left;
        } else {
            return null;
        }
    }

    @Override
    public void collapse() {
    }

    @Override
    public Collection<E> selfCopy(int copies) {
        if (copies < 1) {
            return emptyList();
        }

        List<E> clones = range(0, copies).mapToObj(i -> selfCopy()).collect(toCollection(() -> new ArrayList<>(copies)));
        return clones;
    }

    @Override
    public E selfCopy() {
        E edge = getGraph().connectNodes(left, right, link);
        return edge;
    }

    @Override
    public Split<N, E> insertMiddleNode(N middleNode) {
        if (middleNode == null) {
            throw new NullPointerException("Can not insert an undefined node.");
        }

        getGraph().breakEdge((E) this);

        //TODO: Should we preserve data? Does this make sense? If the data is a context-sensitive or unique?
        //E leftEdge = getGraph().connectNodes(left, middleNode, link);
        E leftEdge = getGraph().connectNodes(left, middleNode, link);

        E rightEdge = getGraph().connectNodes(middleNode, right, link);

        Split split = new SplitImpl(leftEdge, rightEdge);

        return split;
    }

    @Override
    public String toString() {
        return left.toString() + " -" + ((link == Link.DIRECTED) ? '>' : '-') + ' ' + right.toString();
    }

    public static class SplitImpl<N extends Node<N, E>, E extends Edge<N, E>> implements Split<N, E> {

        private final E leftEdge;

        private final E rightEdge;

        private Object edgePayload;

        private SplitImpl() {
            this.leftEdge = null;
            this.rightEdge = null;
        }

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

        @Override
        public Object getEdgePayload() {
            return edgePayload;
        }

        @Override
        public Split<N, E> setEdgePayload(Object edgePayload) {
            this.edgePayload = edgePayload;
            return this;
        }

    }
}
