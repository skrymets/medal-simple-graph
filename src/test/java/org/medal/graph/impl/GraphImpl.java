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
package org.medal.graph.impl;

import org.medal.graph.Edge;
import org.medal.graph.EdgeFactory;
import org.medal.graph.IDProvider;
import org.medal.graph.NodeFactory;
import org.medal.graph.id.NumberIDProvider;

public class GraphImpl extends AbstractGraph<Long, String, String, NodeImpl, EdgeImpl> {

    protected final NumberIDProvider idProvider = new NumberIDProvider();

    @Override
    protected NodeFactory<Long, String, String, NodeImpl, EdgeImpl> getNodeFactory() {
        return () -> new NodeImpl(GraphImpl.this);
    }

    @Override
    protected EdgeFactory<Long, String, String, NodeImpl, EdgeImpl> getEdgeFactory() {
        return (NodeImpl left, NodeImpl right, Edge.Link direction) -> new EdgeImpl(GraphImpl.this, left, right, direction);
    }

    @Override
    protected IDProvider<Long> getIdProvider() {
        return idProvider;
    }

}
