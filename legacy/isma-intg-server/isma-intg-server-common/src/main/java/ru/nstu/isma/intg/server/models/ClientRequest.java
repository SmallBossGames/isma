package ru.nstu.isma.intg.server.models;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Maria Nasyrova
 * @since 29.08.2015
 */
public class ClientRequest<T> implements Serializable {

    private final Request<T> request;
    private final Map<String, byte[]> serializedClasses;

    public ClientRequest(Request<T> request, Map<String, byte[]> serializedClasses) {
        this.request = request;
        this.serializedClasses = serializedClasses;
    }

    public Request<T> getRequest() {
        return request;
    }

    public Map<String, byte[]> getSerializedClasses() {
        return serializedClasses;
    }

}
