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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Edge extends DataObject {

    public static final Edge UNDEFINED = new Edge(Node.UNDEFINED, Node.UNDEFINED, Direction.UNDIRECT);

    public static class Split {

        public static final Split UNDEFINED = new Split(Edge.UNDEFINED, Edge.UNDEFINED);

        private final Edge leftEdge;
        private final Edge rightEdge;

        public Split(Edge leftEdge, Edge rightEdge) {
            this.leftEdge = leftEdge;
            this.rightEdge = rightEdge;
        }

        public Edge getLeftEdge() {
            return leftEdge;
        }

        public Edge getRightEdge() {
            return rightEdge;
        }

    }

    /**
     * "Direct" means that imaginary arrow points from LEFT to RIGHT node
     * (L) ----> (R)
     * According to this definition another definition emerges:
     * 1) A DIRECT edge is OUTGOING for LEFT, and is INCOMING for RIGHT nodes
     * 2) A UNDIRECT edge is neither OUTGOING nor INCOMING for any node
     */
    public static enum Direction {
        DIRECT,
        UNDIRECT
    }

    private final Node left;
    
    private final Node right;

    private Direction direction;

    Edge(Node left, Node right, Direction direction) {
        super();
        
        if (left == null || right == null) {
            throw new IllegalArgumentException("Neither left or right note can be undefined.");
        }
        this.left = left;
        this.right = right;
        this.direction = (direction == null) ? Direction.UNDIRECT : direction;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Direction getDirection() {
        return direction;
    }

    public Edge setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public Node getOpposite(Node node) {
        if (left.equals(node)) { //TODO: Equals or == ?
            return right;
        } else if (right.equals(node)) {
            return left;
        } else {
            return Node.UNDEFINED;
        }
    }

    public void collapse() {
    }

    public Collection<Edge> selfCopy(int copies) {
        if (copies < 1) {
            return Collections.emptyList();
        }

        List<Edge> clones = new ArrayList<>(copies);
        for (int i = 0; i < copies; i++) {
            clones.add(selfCopy());
        }
        return clones;
    }

    public Edge selfCopy() {
        Edge edge = Graph.connectNodes(left, right, direction);
        edge.setPayload(payload);
        return edge;
    }

    public Split insertMiddleNode(Node middleNode) {
        if (middleNode == null || middleNode == Node.UNDEFINED) {
            return Split.UNDEFINED;
        }

        Graph.composite(left.getGraph(), right.getGraph()).breakEdge(this);

        Edge leftEdge = Graph.connectNodes(left, middleNode, direction);
        leftEdge.setPayload(payload);
        Edge rightEdge = Graph.connectNodes(middleNode, right, direction);
        rightEdge.setPayload(payload);
        
        return new Split(leftEdge, rightEdge);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.left);
        hash = 47 * hash + Objects.hashCode(this.right);
        hash = 47 * hash + Objects.hashCode(this.direction);
        // Payload MUST NOT participate in the hash!
        // hash = 47 * hash + Objects.hashCode(this.payload); 
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        //TODO: Consider ID in equality (from business point of view)
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        if (this.direction != other.direction) {
            return false;
        }
        // Payload MUST NOT participate in the eguals!
//        if (!Objects.equals(this.payload, other.payload)) {
//            return false;
//        }
        return true;
    }

    @Override
    public String toString() {
        return left.toString() + " -" + ((direction == Direction.DIRECT) ? '>' : '-') + ' ' + right.toString();
    }

}
