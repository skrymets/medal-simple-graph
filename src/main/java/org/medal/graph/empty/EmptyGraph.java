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

import java.util.Collections;
import java.util.Set;
import org.medal.graph.api.IGraph;
import org.medal.graph.api.INode;
import org.medal.graph.api.IEdge;

public enum EmptyGraph implements IGraph {

    INSTANCE;

    @Override
    public void breakEdge(IEdge edge) {
    }

    @Override
    public INode createNode(Object payload) {
        return EmptyNode.INSTANCE;
    }

    @Override
    public INode createNode() {
        return EmptyNode.INSTANCE;
    }

    @Override
    public Set<INode> createNodes(int count) {
        return Collections.emptySet();
    }

    @Override
    public IEdge connectNodes(INode left, INode right, IEdge.Link direction) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public IEdge connectNodes(INode left, INode right) {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Set<IEdge> getEdges() {
        return Collections.emptySet();
    }

    @Override
    public Set<INode> getNodes() {
        return Collections.emptySet();
    }

    @Override
    public void deleteNode(INode node) {
    }


}
