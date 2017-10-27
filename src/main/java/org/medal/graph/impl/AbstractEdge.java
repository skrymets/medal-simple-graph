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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.medal.graph.Edge.Link;
import org.medal.graph.Split;
import org.medal.graph.empty.EmptyNode;
import org.medal.graph.Node;
import org.medal.graph.Graph;
import org.medal.graph.Edge;

abstract class AbstractEdge<I, D> extends AbstractDataObject<I, D> implements Edge<I, D> {

    protected final Node<I, D> left;

    protected final Node<I, D> right;

    protected Link link;

    protected final Graph<I, D> graph;

    AbstractEdge(Graph<I, D> graph, Node<I, D> left, Node<I, D> right, Link link) {

        Objects.requireNonNull(graph);
        if (left == null || right == null) {
            throw new IllegalArgumentException("Neither left or right note can be undefined.");
        }

        this.graph = graph;
        this.left = left;
        this.right = right;
        this.link = (link == null) ? Link.UNDIRECTED : link;
    }

    @Override
    public Node<I, D> getLeft() {
        return left;
    }

    @Override
    public Node<I, D> getRight() {
        return right;
    }

    @Override
    public Link getDirected() {
        return link;
    }

    @Override
    public Graph<I, D> getGraph() {
        return graph;
    }

    @Override
    public Edge<I, D> setDirected(Link direction) {
        this.link = direction;
        return this;
    }

    @Override
    public Node<I, D> getOpposite(Node<I, D> node) {
        if (left.equals(node)) { //TODO: Equals or == ?
            return right;
        } else if (right.equals(node)) {
            return left;
        } else {
            return EmptyNode.INSTANCE;
        }
    }

    @Override
    public void collapse() {
    }

    @Override
    public Collection<Edge<I, D>> selfCopy(int copies) {
        if (copies < 1) {
            return Collections.emptyList();
        }

        List<Edge<I, D>> clones = new ArrayList<>(copies);
        for (int i = 0; i < copies; i++) {
            clones.add(selfCopy());
        }
        return clones;
    }

    @Override
    public Edge<I, D> selfCopy() {
        Edge<I, D> edge = getGraph().connectNodes(left, right, link);
        edge.setData(this.getData());
        return edge;
    }

    @Override
    public Split<I, D> insertMiddleNode(Node<I, D> middleNode) {
        if (middleNode == null || middleNode == EmptyNode.INSTANCE) {
            return Split.UNDEFINED;
        }

        getGraph().breakEdge(this);

        //TODO: Should we preserve data? Does this make sense? If the data is a context-seisitive or unique?
        Edge<I, D> leftEdge = getGraph().connectNodes(left, middleNode, link);
        leftEdge.setData(this.getData());
        Edge<I, D> rightEdge = getGraph().connectNodes(middleNode, right, link);
        rightEdge.setData(this.getData());

        return new Split<>(leftEdge, rightEdge);
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

}
