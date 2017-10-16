package org.medal.graph;

import java.util.UUID;

public abstract class DataObject<P> {

    protected Object id;

    protected P payload;

    public DataObject() {
        id = UUID.randomUUID().toString();
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

}
