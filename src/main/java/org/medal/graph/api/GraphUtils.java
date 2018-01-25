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
package org.medal.graph.api;

import static java.util.Arrays.asList;
import java.util.HashSet;
import static java.util.Objects.requireNonNull;
import org.medal.graph.Graph;
import org.medal.graph.Node;

/**
 *
 * @author skrymets
 */
public class GraphUtils {

    /**
     * Two nodes of a graph are called adjacent if they share a common edge (regardless of
     * whether it is directional or not.)
     *
     * @return <code>true</code> if both <code>node1</code> and <code>node2</code> are
     *         adjacent.
     *
     * @throws NullPointerException if <code>node1</code> or <code>node2</code> is
     *                              <code>null</code>.
     */
    public static boolean nodesAreAdjacent(INode node1, INode node2) {
        requireNonNull(node1);
        requireNonNull(node2);

        return node1.getEdgesToNode(node2).size() > 0;

    }

    /**
     * Two edges of a graph are called adjacent if they share a common vertex
     *
     * @return <code>true</code> if both <code>edge1</code> and <code>edge2</code> are
     *         adjacent.
     *
     * @throws NullPointerException if <code>edge1</code> or <code>edge2</code> is
     *                              <code>null</code>.
     */
    public static boolean edgesAreAdjacent(IEdge edge1, IEdge edge2) {
        requireNonNull(edge1);
        requireNonNull(edge2);

        if (edge1 == edge2) {
            return false;
        }

        return (new HashSet<>(
                asList(edge1.getLeft(), edge1.getRight(),
                        edge2.getLeft(), edge2.getRight()
                ))).size() < 4;
    }

    /**
     * Creates a graph that is a grid of <code>rows</code> by <code>columns</code>
     * vertices that are non-directionally linked in "horizontally" and "vertically" in a
     * grid.
     *
     * <pre>
     *
     * (*) --- (*) --- ... --- (*)
     *  |       |               |
     * (*) --- (*) --- ... --- (*)
     *  |       |               |
     * ...     ...     ...     ...
     *  |       |               |
     * (*) --- (*) --- ... --- (*)
     *
     * </pre>
     *
     * @param <N>     Nodes' payload type
     * @param <E>     Edges' payload type
     * @param rows    a number of rows in the net
     * @param columns a number of columns in the net
     *
     * @return a graph instance that contains <code>rows</code> * <code>columns</code>
     *         vertices and <code>((ROWS - 1) * COLS) + (ROWS * (COLS - 1))</code> edges.
     *
     * @throws IllegalArgumentException if any of a dimension is negative.
     */
    public static <N, E> Graph<N, E> createGridGraph(int rows, int columns) {
        if (rows < 0 || columns < 0) {
            throw new IllegalArgumentException("All dimensions have to be greater than zero.");
        }

        Graph<N, E> graph = new Graph<>();
        if (rows == 0 || columns == 0) {
            return graph;
        }

        Node[][] nodes = new Node[rows][columns];
        for (int row = 0; row < rows; row++) {
            Node<N, E> firstInRow = graph.createNode();
            nodes[row][0] = firstInRow;
            if (row > 0) {
                firstInRow.connect(nodes[row - 1][0]);
            }
            for (int col = 1; col < columns; col++) {
                // The loop starts from 1 to scip the very first node in the row,
                // as it was created.
                // Create new
                Node<N, E> current = graph.createNode();
                // Store the node
                nodes[row][col] = current;
                // Link this node with a previos in this row
                nodes[row][col - 1].connect(current);
                if (row == 0) {
                    continue;
                }
                // Link this node with a previos in the same column
                nodes[row - 1][col].connect(current);
            }
        }
        return graph;
    }

}
