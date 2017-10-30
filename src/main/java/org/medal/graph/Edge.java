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

public interface Edge<I, N extends Node<I, ?>, EP> extends DataObject<I, EP> {

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

    <E extends Edge<I, N, EP>> Graph<I, ?, EP, N, E> getGraph();

    N getOpposite(N node);

    N getRight();

    N getLeft();

    <E extends Edge<I, N, EP>> E setDirected(Link direction);

    <E extends Edge<I, N, EP>> E selfCopy();

    <E extends Edge<I, N, EP>> Collection<E> selfCopy(int copies);

    <E extends Edge<I, N, EP>> Split<I, ?, EP, N, E> insertMiddleNode(N middleNode);

}
