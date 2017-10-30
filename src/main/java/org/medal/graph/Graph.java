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
import org.medal.graph.Edge.Link;

public interface Graph<I, NP, EP, N extends Node<I, NP>, E extends Edge<I, N, EP>> {

    N createNode(NP payload);

    N createNode();

    /**
     * Creates several new nodes that are not connected at this moment.
     *
     * @param count a number of nodes to create
     *
     * @return a list of nodes that were created or an empty list, if <code>count</code>
     *         is less or equal to zero.
     */
    Collection<N> createNodes(int count);

    E connectNodes(N left, N right, Link direction);

    E connectNodes(N left, N right);

    Set<E> getEdges();

    Set<N> getNodes();

    void breakEdge(Edge edge);

}
