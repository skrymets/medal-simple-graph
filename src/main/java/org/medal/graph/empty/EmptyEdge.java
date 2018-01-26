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
import org.medal.graph.api.Split;
import org.medal.graph.api.IGraph;
import org.medal.graph.api.INode;
import org.medal.graph.api.IEdge;

public enum EmptyEdge implements IEdge {

    INSTANCE;

    private final static Object EMPTY = new Object();

    @Override
    public void collapse() {
    }

    @Override
    public IGraph getGraph() {
        return EmptyGraph.INSTANCE;
    }

    @Override
    public Link getDirected() {
        return Link.UNDIRECTED;
    }

    @Override
    public IEdge setDirected(Link direction) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public INode getLeft() {
        return EmptyNode.INSTANCE;
    }

    @Override
    public INode getOpposite(INode node) {
        return EmptyNode.INSTANCE;
    }

    @Override
    public INode getRight() {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Split insertMiddleNode(INode middleNode) {
        return Split.UNDEFINED;
    }

    @Override
    public Collection<IEdge> selfCopy(int copies) {
        return Collections.emptySet();
    }

    @Override
    public IEdge selfCopy() {
        return INSTANCE;
    }

    @Override
    public Object getId() {
        return EMPTY;
    }

    @Override
    public EmptyEdge setId(Object id) {
        return this;
    }

    @Override
    public Object getData() {
        return EMPTY;
    }

    @Override
    public EmptyEdge setData(Object data) {
        return this;
    }

}
