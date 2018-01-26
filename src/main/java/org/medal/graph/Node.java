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
import java.util.Set;
import org.medal.graph.api.AbstractNode;
import org.medal.graph.api.IGraph;

public class Node<N, E> extends AbstractNode<Long, N, E, Node<N, E>, Edge<N, E>> {

    Node(Graph<N, E> graph) {
        super(graph);
    }

    @Override
    public Set<Edge<N, E>> getEdgesToNode(Node<N, E> destination) {
        return super.getEdgesToNode(destination); 
    }

    @Override
    public Set<Node<N, E>> getLinkedNodes() {
        return super.getLinkedNodes(); 
    }

    @Override
    public Edge<N, E> connect(Node<N, E> otherNode) {
        return super.connect(otherNode); 
    }

    @Override
    public Edge<N, E> connectNodeFromLeft(Node<N, E> leftNode) {
        return super.connectNodeFromLeft(leftNode); 
    }

    @Override
    public Edge<N, E> connectNodeFromRight(Node<N, E> rightNode) {
        return super.connectNodeFromRight(rightNode); 
    }

    @Override
    public IGraph<Long, N, E, Node<N, E>, Edge<N, E>> getGraph() {
        return super.getGraph(); 
    }

    @Override
    public Collection<Edge<N, E>> getOutgoingEdges(boolean includeUndirected) {
        return super.getOutgoingEdges(includeUndirected); 
    }

    @Override
    public Collection<Edge<N, E>> getIncomingEdges(boolean includeUndirected) {
        return super.getIncomingEdges(includeUndirected); 
    }

    @Override
    public Collection<Edge<N, E>> getOutgoingEdges() {
        return super.getOutgoingEdges(); 
    }

    @Override
    public Collection<Edge<N, E>> getIncomingEdges() {
        return super.getIncomingEdges(); 
    }

    @Override
    public Collection<Edge<N, E>> getEdges() {
        return super.getEdges(); 
    }

    @Override
    public Node<N, E> setData(N data) {
        super.setData(data); 
        return this;
    }

    @Override
    public N getData() {
        return super.getData(); 
    }

    @Override
    public Node<N, E> setId(Long id) {
        super.setId(id); 
        return this;
    }

    @Override
    public Long getId() {
        return super.getId(); 
    }
    
    
    
}
