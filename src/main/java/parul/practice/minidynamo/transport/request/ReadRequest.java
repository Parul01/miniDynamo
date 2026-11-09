package parul.practice.minidynamo.transport.request;

import parul.practice.minidynamo.transport.Request;

public class ReadRequest implements Request {

    private final String key;

    public ReadRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String action() {
        return "client/read";
    }
}
