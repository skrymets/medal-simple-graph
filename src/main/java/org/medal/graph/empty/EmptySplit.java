package org.medal.graph.empty;

import org.medal.graph.Edge;

public enum EmptySplit implements Edge.Split {
    INSTANCE;

    @Override
    public Edge getLeftEdge() {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Edge getRightEdge() {
        return EmptyEdge.INSTANCE;
    }

    @Override
    public Object getEdgePayload() {
        return null;
    }

    @Override
    public Edge.Split setEdgePayload(Object edgePayload) {
        return this;
    }
}
