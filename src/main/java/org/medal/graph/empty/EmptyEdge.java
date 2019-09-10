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
import org.medal.graph.Split;

import java.util.Collection;
import java.util.Collections;

public enum EmptyEdge implements Edge {

    INSTANCE;

    private final static Object EMPTY = new Object();

    @Override
    public void collapse() {
    }

    @Override
    public Graph getGraph() {
        return EmptyGraph.INSTANCE;
    }

    @Override
    public Link getDirected() {
        return Link.UNDIRECTED;
    }

    @Override
    public Edge setDirected(Link direction) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Node getLeft() {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Node getOpposite(Node node) {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Node getRight() {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Split insertMiddleNode(Node middleNode) {
        return Split.UNDEFINED;
    }

    @Override
    public Collection<Edge> selfCopy(int copies) {
        return Collections.emptySet();
    }

    @Override
    public Edge selfCopy() {
        return INSTANCE;
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
