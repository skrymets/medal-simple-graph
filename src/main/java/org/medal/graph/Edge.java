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
import org.medal.graph.api.AbstractEdge;
import org.medal.graph.api.IEdge.Link;
import org.medal.graph.api.IGraph;
import org.medal.graph.api.Split;

public class Edge<N, E> extends AbstractEdge<Long, N, E, Node<N, E>, Edge<N, E>> {

    Edge(Graph<N, E> graph, Node<N, E> left, Node<N, E> right, Link link) {
        super(graph, left, right, link);
    }

    @Override
    public Split<Long, N, E, Node<N, E>, Edge<N, E>> insertMiddleNode(Node<N, E> middleNode) {
        return super.insertMiddleNode(middleNode);
    }

    @Override
    public Edge<N, E> selfCopy() {
        return super.selfCopy();
    }

    @Override
    public Collection<Edge<N, E>> selfCopy(int copies) {
        return super.selfCopy(copies);
    }

    @Override
    public Node<N, E> getOpposite(Node<N, E> node) {
        return super.getOpposite(node);
    }

    @Override
    public Edge<N, E> setDirected(Link direction) {
        return super.setDirected(direction);
    }

    @Override
    public IGraph<Long, N, E, Node<N, E>, Edge<N, E>> getGraph() {
        return super.getGraph();
    }

    @Override
    public Node<N, E> getRight() {
        return super.getRight();
    }

    @Override
    public Node<N, E> getLeft() {
        return super.getLeft();
    }

    @Override
    public void setData(E data) {
        super.setData(data);
    }

    @Override
    public E getData() {
        return super.getData();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

}
