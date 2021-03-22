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

import java.util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;

public abstract class AbstractEdge<I, N extends Node<I, N, E>, E extends Edge<I, N, E>> extends AbstractDataObject<I> implements Edge<I, N, E> {

    protected final N left;

    protected final N right;

    protected Link link;

    private final Graph<I, N, E> graph;

    public AbstractEdge(
            Graph<I, N, E> graph,
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
    public Graph<I, N, E> getGraph() {
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
        edge.setData(this.getData());
        return edge;
    }

    @Override
    public Split<I, N, E> insertMiddleNode(N middleNode) {
        if (middleNode == null) {
            throw new NullPointerException("Can not insert an undefined node.");
        }

        getGraph().breakEdge((E) this);

        //TODO: Should we preserve data? Does this make sense? If the data is a context-sensitive or unique?
        //E leftEdge = getGraph().connectNodes(left, middleNode, link);
        E leftEdge = getGraph().connectNodes(left, middleNode, link);
        leftEdge.setData(this.getData());

        E rightEdge = getGraph().connectNodes(middleNode, right, link);
        rightEdge.setData(this.getData());

        Split split = new SplitImpl(leftEdge, rightEdge);
        split.setEdgePayload(getData()); // Preserve the payload

        return split;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.left);
        hash = 47 * hash + Objects.hashCode(this.right);
        hash = 47 * hash + Objects.hashCode(this.link);
        // Payload MUST NOT participate in the hash!
        // hash = 47 * hash + Objects.hashCode(this.payload); 
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
        final AbstractEdge other = (AbstractEdge) obj;
        //TODO: Consider ID in equality (from business point of view)
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        if (this.link != other.link) {
            return false;
        }
        // Payload MUST NOT participate in the eguals!
//        if (!Objects.equals(this.payload, other.payload)) {
//            return false;
//        }
        return true;
    }

    @Override
    public String toString() {
        return left.toString() + " -" + ((link == Link.DIRECTED) ? '>' : '-') + ' ' + right.toString();
    }

    public static class SplitImpl<I, NP, EP, N extends Node<I, NP, EP, N, E>, E extends Edge<I, NP, EP, N, E>> implements Split<I, NP, EP, N, E> {

        private final E leftEdge;

        private final E rightEdge;

        private EP edgePayload;

        private SplitImpl() {
            this.leftEdge = null;
            this.rightEdge = null;
        }

        private SplitImpl(E leftEdge, E rightEdge) {
            Objects.requireNonNull(leftEdge);
            Objects.requireNonNull(rightEdge);

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
        public EP getEdgePayload() {
            return edgePayload;
        }

        @Override
        public Split<I, NP, EP, N, E> setEdgePayload(EP edgePayload) {
            this.edgePayload = edgePayload;
            return this;
        }

    }
}
