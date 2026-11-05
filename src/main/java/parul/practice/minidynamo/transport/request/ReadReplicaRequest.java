package parul.practice.minidynamo.transport.request;

import parul.practice.minidynamo.transport.Request;

public class ReadReplicaRequest implements Request {
    private final String key;

    public ReadReplicaRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String action() {
        return "replica/read";
    }
}
