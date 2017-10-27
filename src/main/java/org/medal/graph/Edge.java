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

public interface Edge<I, D> extends DataObject<I, D> {

    /**
     * "Link" means that imaginary arrow points from LEFT to RIGHT node
     * (L) ----> (R)
     * According to this definition another definition emerges:
     * 1) A DIRECTED edge is OUTGOING for LEFT, and is INCOMING for RIGHT nodes
     * 2) A UNDIRECTED edge is neither OUTGOING nor INCOMING for any node
     */
    public enum Link {
        DIRECTED, UNDIRECTED
    }

    void collapse();

    Link getDirected();

    Graph<I, D> getGraph();

    Node<I, D> getOpposite(Node<I, D> node);

    Node<I, D> getRight();

    Node<I, D> getLeft();

    Edge<I, D> setDirected(Link direction);

    Edge<I, D> selfCopy();

    Collection<Edge<I, D>> selfCopy(int copies);

    Split<I, D> insertMiddleNode(Node<I, D> middleNode);

}
