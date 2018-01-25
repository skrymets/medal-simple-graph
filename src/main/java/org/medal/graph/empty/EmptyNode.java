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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.medal.graph.api.IGraph;
import org.medal.graph.api.INode;
import org.medal.graph.api.IEdge;

public enum EmptyNode implements INode {

    INSTANCE;

    private final static Object EMPTY = new Object();

    @Override
    public IEdge connect(INode otherNode) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public IEdge connectNodeFromLeft(INode leftNode) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public IEdge connectNodeFromRight(INode rightNode) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Collection<IEdge> getEdges() {
        return Collections.emptySet();
    }

    @Override
    public Set<IEdge> getEdgesToNode(INode destination) {
        return Collections.emptySet();
    }

    @Override
    public IGraph getGraph() {
        return EmptyGraph.INSTANCE;
    }

    @Override
    public Collection<IEdge> getIncomingEdges() {
        return Collections.emptySet();
    }

    @Override
    public Collection<IEdge> getIncomingEdges(boolean includeUndirected) {
        return Collections.emptySet();
    }

    @Override
    public Set<INode> getLinkedNodes() {
        return Collections.emptySet();
    }

    @Override
    public Collection<IEdge> getOutgoingEdges() {
        return Collections.emptySet();
    }

    @Override
    public Collection<IEdge> getOutgoingEdges(boolean includeUndirected) {
        return Collections.emptySet();
    }

    @Override
    public Object getId() {
        return EMPTY;
    }

    @Override
    public Object getData() {
        return EMPTY;
    }

    @Override
    public void setId(Object id) {
    }

    @Override
    public void setData(Object data) {
    }

}
