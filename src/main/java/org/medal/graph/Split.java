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

import org.medal.graph.empty.EmptyEdge;

public class Split<I, NP, EP, N extends Node<I, NP>, E extends Edge<I, ?, EP>> {

    public static final Split UNDEFINED = new Split(EmptyEdge.INSTANCE, EmptyEdge.INSTANCE);

    private final E leftEdge;

    private final E rightEdge;

    public Split(E leftEdge, E rightEdge) {
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    public E getLeftEdge() {
        return leftEdge;
    }

    public E getRightEdge() {
        return rightEdge;
    }

}
