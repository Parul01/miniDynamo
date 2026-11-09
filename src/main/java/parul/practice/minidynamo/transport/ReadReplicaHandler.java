package parul.practice.minidynamo.transport;

import parul.practice.minidynamo.storage.StorageEngine;

public class ReadReplicaHandler implements RequestHandler{

    private final StorageEngine storageEngine;

    public ReadReplicaHandler(StorageEngine engine) {
        this.storageEngine = engine;
    }

    @Override
    public Response handle(Request request) {
        // handle replica reads

    }
}
