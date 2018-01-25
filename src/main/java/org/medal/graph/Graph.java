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

import java.util.Set;
import org.medal.graph.api.AbstractGraph;
import org.medal.graph.api.EdgeFactory;
import org.medal.graph.api.IDProvider;
import org.medal.graph.api.NodeFactory;
import org.medal.graph.id.NumberIDProvider;
import org.medal.graph.api.IEdge;

public class Graph<N, E> extends AbstractGraph<Long, N, E, Node<N, E>, Edge<N, E>> {

    protected final NumberIDProvider nidp = new NumberIDProvider();

    @Override
    protected NodeFactory<Long, N, E, Node<N, E>, Edge<N, E>> getNodeFactory() {
        return () -> new Node<>(Graph.this);
    }

    @Override
    protected EdgeFactory<Long, N, E, Node<N, E>, Edge<N, E>> getEdgeFactory() {
        return (Node<N, E> left, Node<N, E> right, IEdge.Link direction) -> new Edge(Graph.this, left, right, direction);
    }

    @Override
    protected IDProvider<Long> getIdProvider() {
        return nidp;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Edge<N, E> connectNodes(Node<N, E> left, Node<N, E> right) {
        return super.connectNodes(left, right);
    }

    @Override
    public Edge<N, E> connectNodes(Node<N, E> left, Node<N, E> right, IEdge.Link direction) {
        return super.connectNodes(left, right, direction);
    }

    @Override
    public Set<Edge<N, E>> getEdges() {
        return super.getEdges();
    }

    @Override
    public Set<Node<N, E>> getNodes() {
        return super.getNodes();
    }

    @Override
    public Set<Node<N, E>> createNodes(int count) {
        return super.createNodes(count);
    }

    @Override
    public Node<N, E> createNode() {
        return super.createNode();
    }

    @Override
    public Node<N, E> createNode(N payload) {
        return super.createNode(payload);
    }

    @Override
    public void breakEdge(Edge<N, E> edge) {
        super.breakEdge(edge);
    }

}
