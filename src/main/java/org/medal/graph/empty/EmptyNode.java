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
package org.medal.graph.empty;

import org.medal.graph.Edge;
import org.medal.graph.Graph;
import org.medal.graph.Node;

import java.util.Collection;
import java.util.Set;

import static java.util.Collections.emptySet;

public enum EmptyNode implements Node {

    INSTANCE;

    private final static Object EMPTY = new Object();

    @Override
    public Edge connect(Node otherNode) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Edge connectNodeFromLeft(Node leftNode) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Edge connectNodeFromRight(Node rightNode) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Collection<Edge> getEdges() {
        return emptySet();
    }

    @Override
    public Set<Edge> getEdgesToNode(Node destination) {
        return emptySet();
    }

    @Override
    public Graph getGraph() {
        return EmptyGraph.INSTANCE;
    }

    @Override
    public Collection<Edge> getIncomingEdges() {
        return emptySet();
    }

    @Override
    public Collection<Edge> getIncomingEdges(boolean includeUndirected) {
        return emptySet();
    }

    @Override
    public Set<Node> getLinkedNodes() {
        return emptySet();
    }

    @Override
    public Collection<Edge> getOutgoingEdges() {
        return emptySet();
    }

    @Override
    public Collection<Edge> getOutgoingEdges(boolean includeUndirected) {
        return emptySet();
    }

    @Override
    public Object getId() {
        return EMPTY;
    }

    @Override
    public void setId(Object id) {
    }

    @Override
    public Object getData() {
        return EMPTY;
    }

    @Override
    public void setData(Object data) {
    }

}
