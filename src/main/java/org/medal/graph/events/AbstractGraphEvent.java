/*
 * Copyright 2019 skrymets.
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
package org.medal.graph.events;

import org.medal.graph.Edge;
import org.medal.graph.Graph;
import org.medal.graph.GraphEvent;
import org.medal.graph.Node;

public class AbstractGraphEvent<I, NP, EP, N extends Node<I, NP, EP, N, E>, E extends Edge<I, NP, EP, N, E>> implements GraphEvent<I, NP, EP, N, E> {

    private final Graph<I, NP, EP, N, E> graph;

    public AbstractGraphEvent(Graph<I, NP, EP, N, E> graph) {
        this.graph = graph;
    }

    @Override
    public Graph<I, NP, EP, N, E> getGraph() {
        return this.graph;
    }


}
