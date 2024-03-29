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

import org.medal.graph.EdgeFactory;
import org.medal.graph.NodeFactory;

public class GraphImpl extends AbstractGraph<NodeImpl, EdgeImpl> {

    @Override
    protected NodeFactory<NodeImpl, EdgeImpl> getNodeFactory() {
        return () -> new NodeImpl(GraphImpl.this);
    }

    @Override
    protected EdgeFactory<NodeImpl, EdgeImpl> getEdgeFactory() {
        return (NodeImpl left, NodeImpl right) -> new EdgeImpl(GraphImpl.this, left, right);
    }

}
