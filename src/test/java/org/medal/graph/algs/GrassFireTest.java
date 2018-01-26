/*
 * Copyright 2018 skrymets.
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
package org.medal.graph.algs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.Before;
import org.medal.graph.Graph;
import org.medal.graph.Node;

/**
 * @author skrymets
 */
public class GrassFireTest {

    public GrassFireTest() {
    }

    @Before
    public void setupData() {
    }

    @Test
    public void testGrassFireAlgorithm() {

        // We don't care about the edges' payload
        final int ROWS = 20;
        final int COLUMNS = 20;

        for (int i = 0; i < 1; i++) {

            GridGraph<Integer, Void> data = GridGraph.createInstance(ROWS, COLUMNS);
            data.getNodes().forEach((Node<Integer, Void> n) -> n.setData(-(ROWS * COLUMNS)));

            Random random = new Random();

            final int startX = random.nextInt(ROWS);
            final int startY = random.nextInt(COLUMNS);
            final int endX = random.nextInt(ROWS);
            final int endY = random.nextInt(COLUMNS);

            final Node<Integer, Void> START_NODE = data.nodeAt(startX, startY);
            final Node<Integer, Void> END_NODE = data.nodeAt(endX, endY);

            // Remove random number of vertices
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLUMNS; c++) {
                    if (random.nextBoolean() && random.nextBoolean()) {
                        final Node<Integer, Void> nodeAt = data.nodeAt(r, c);
                        if (nodeAt == START_NODE || nodeAt == END_NODE) {
                            continue;
                        }
                        data.deleteNode(nodeAt);
                    }
                }
            }

            calculateSteps(START_NODE, END_NODE);
        }

    }

    private void calculateSteps(Node<Integer, Void> start, Node<Integer, Void> end) {
        // -------------------------------------------------------------------------------
        GridGraph<Integer, Void> data = (GridGraph<Integer, Void>) end.getGraph();

        // -------------------------------------------------------------------------------
        // Initialize 
        // -------------------------------------------------------------------------------
        final int[] distance = new int[]{0}; // wrap it to a single-value array to make "final"

        Set<Node<Integer, Void>> visitedNodes = new HashSet<>(data.rows * data.columns);
        end.setData(distance[0]);
        visitedNodes.add(end);

        Set<Node<Integer, Void>> layer = end.getLinkedNodes();

        // -------------------------------------------------------------------------------
        // Fill the distances layer-by-layer 
        // -------------------------------------------------------------------------------
        while (true) {
            if (layer.isEmpty()) {
                break; // There are no more unprocessed nodes. There is nothing to do here anymore;
            }

            layer.stream().forEach((Node<Integer, Void> n) -> n.setData(distance[0] + 1));
            distance[0]++;
            visitedNodes.addAll(layer);
            layer = layer.stream()
                    // .filter(Objects::nonNull)
                    .flatMap((Node<Integer, Void> n) -> n.getLinkedNodes().stream())
                    .filter((Node<Integer, Void> n) -> !visitedNodes.contains(n))
                    .collect(Collectors.toSet());

            // System.out.println(data.toString());
        }
        
        System.out.println(data.toString());

        // -------------------------------------------------------------------------------
        // Find the path
        // -------------------------------------------------------------------------------
        // Mark it as a path's step (*)
        visitedNodes.clear();
        layer.clear();
        distance[0] = start.getData();

        start.setData(null);

        visitedNodes.add(start);

        layer = start.getLinkedNodes();
        boolean finished = false;

        while (true) {

            for (Node<Integer, Void> node : layer) {

                // System.out.println(data.toString());
                if (node == end) {
                    finished = true;
                    break;
                }

                visitedNodes.add(node);
                if (node.getData() < distance[0]) {
                    distance[0] = node.getData();
                    node.setData(null);

                    layer = node.getLinkedNodes()
                            .stream()
                            .filter((Node<Integer, Void> n) -> !visitedNodes.contains(n))
                            .collect(Collectors.toSet());

                    break;
                }
            }

            if (layer.isEmpty() || finished) {
                break; // There are no more unprocessed nodes. There is nothing to do here anymore;
            }
        }

        // -------------------------------------------------------------------------------
        // Print the results
        // -------------------------------------------------------------------------------
        System.out.println(data.toString());
    }

    static class GridGraph<N, E> extends Graph<N, E> {

        private final int rows;
        private final int columns;

        private final Node<N, E>[][] nodesIndex;

        private GridGraph(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;

            this.nodesIndex = new Node[rows][columns];
        }

        public Node<N, E> nodeAt(int row, int column) {
            return nodesIndex[row][column];
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }

        private void cantCreateNodes() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("This type of graph does not support node creation after it was initialized.");
        }

        private Node<N, E> specialCreateNode() {
            return super.createNode(null);
        }

        @Override
        public Node<N, E> createNode(N payload) {
            cantCreateNodes();
            return null;
        }

        @Override
        public Node<N, E> createNode() {
            cantCreateNodes();
            return null;
        }

        @Override
        public Set<Node<N, E>> createNodes(int count) {
            cantCreateNodes();
            return null;
        }

        @Override
        public void deleteNode(Node<N, E> node) {

            for (Node[] row : nodesIndex) {
                for (int j = 0; j < row.length; j++) {
                    Node n = row[j];
                    if (node == n) {
                        super.deleteNode(node);
                        row[j] = null;
                        return;
                    }
                }
            }
        }

        @Override
        public String toString() {

            int maxLength = 0;
            final String PLACEHOLDER = " ## ";

            for (Node<N, E>[] row : nodesIndex) {
                for (Node<N, E> node : row) {
                    if (node == null) {
                        continue;
                    }
                    int length = ((node.getData() == null)
                            ? PLACEHOLDER
                            : node.getData().toString()).length();
                    maxLength = Math.max(maxLength, length);
                }
            }

            final String formatPattern = "%1$" + (maxLength) + "s";

            StringBuilder sb = new StringBuilder("\n\n");
            for (Node[] row : nodesIndex) {
                for (Node node : row) {
                    sb.append("|");
                    if (node == null) {
                        sb.append(String.format(formatPattern, "    "));
                    } else {
                        sb.append(String.format(
                                formatPattern,
                                (node.getData() == null)
                                        ? PLACEHOLDER
                                        : node.getData())
                        );

                    }

                }
                sb.append("\n");
            }

            return sb.toString();
        }

        static <N, E> GridGraph<N, E> createInstance(int rows, int columns) {
            if (rows < 0 || columns < 0) {
                throw new IllegalArgumentException("All dimensions have to be greater than zero.");
            }

            GridGraph<N, E> graph = new GridGraph<>(rows, columns);

            if (rows == 0 || columns == 0) {
                return graph;
            }

            for (int row = 0; row < rows; row++) {
                Node<N, E> firstInRow = graph.specialCreateNode();
                graph.nodesIndex[row][0] = firstInRow;
                if (row > 0) {
                    firstInRow.connect(graph.nodesIndex[row - 1][0]);
                }
                for (int col = 1; col < columns; col++) {
                    // The loop starts from 1 to scip the very first node in the row,
                    // as it was created.
                    // Create new
                    Node<N, E> current = graph.specialCreateNode();
                    // Store the node
                    graph.nodesIndex[row][col] = current;
                    // Link this node with a previos in this row
                    graph.nodesIndex[row][col - 1].connect(current);
                    if (row == 0) {
                        continue;
                    }
                    // Link this node with a previos in the same column
                    graph.nodesIndex[row - 1][col].connect(current);
                }
            }
            return graph;
        }
    }

}
