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

public interface Edge<I, NP, EP, N extends Node<I, NP, EP, N, E>, E extends Edge<I, NP, EP, N, E>> extends DataObject<I, EP> {

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

    /**
     * Returns a graph instance which this edge belongs to
     *
     * @return a graph instance, never <code>null</code>
     */
    Graph<I, ?, EP, N, E> getGraph();

    /**
     * Return this edge's direction attribute.
     *
     * @return direction attribute, never <code>null</code>
     */
    Link getDirected();

    /**
     * Set this edge's direction attribute. If the <code>direction</code> value is
     * <code>null</code>, then actual value will be set to <code>UNDIRECTED</code>
     *
     * @param direction attribute value
     *
     * @return this edge reference
     */
    E setDirected(Link direction);

    /**
     * Returns a node that is linked to the specified <code>node</code> by this edge.
     *
     * @return a <code>Node</code> instance on the other side of this edge if the
     *         specified <code>node</code> belongs to this edge, otherwise - <code>null</code>
     */
    N getOpposite(N node);
    
    /**
     * Return a node that resides in left position of this edge.
     *
     * @return left node, never not <code>null</code>
     */
    N getLeft();
    
    /**
     * Return a node that resides in right position of this edge.
     *
     * @return right node, never not <code>null</code>
     */
    N getRight();
    
    void collapse();

    E selfCopy();

    Collection<E> selfCopy(int copies);

    Split<I, NP, EP, N, E> insertMiddleNode(N middleNode);

}
