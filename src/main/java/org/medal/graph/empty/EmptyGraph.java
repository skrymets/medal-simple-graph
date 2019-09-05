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
import org.medal.graph.events.GraphEventsSubscriber;

import java.util.Set;

import static java.util.Collections.emptySet;

public enum EmptyGraph implements Graph {

    INSTANCE;

    @Override
    public void breakEdge(Edge edge) {
    }

    @Override
    public Node createNode(Object payload) {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Node createNode() {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Set<Node> createNodes(int count) {
        return emptySet();
    }

    @Override
    public Edge connectNodes(Node left, Node right, Edge.Link direction) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Edge connectNodes(Node left, Node right) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Set<Edge> getEdges() {
        return emptySet();
    }

    @Override
    public Set<Node> getNodes() {
        return emptySet();
    }

    @Override
    public void removeSubscriber(GraphEventsSubscriber subscriber) {
    }

    @Override
    public void addSubscriber(GraphEventsSubscriber subscriber) {
    }
}
