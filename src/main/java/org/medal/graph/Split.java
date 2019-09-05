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

import java.util.Objects;

public class Split<I, N extends Node<I, N, E>, E extends Edge<I, N, E>> {

    public static final Split UNDEFINED = new Split();

    private final E leftEdge;

    private final E rightEdge;
    
    private Object edgePayload;

    private Split() {
        this.leftEdge = null;
        this.rightEdge = null;
    }

    public Split(E leftEdge, E rightEdge) {
        Objects.requireNonNull(leftEdge);
        Objects.requireNonNull(rightEdge);

        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    public E getLeftEdge() {
        return leftEdge;
    }

    public E getRightEdge() {
        return rightEdge;
    }

    public Object getEdgePayload() {
        return edgePayload;
    }

    public Split<I, N, E> setEdgePayload(Object edgePayload) {
        this.edgePayload = edgePayload;
        return this;
    }

}
