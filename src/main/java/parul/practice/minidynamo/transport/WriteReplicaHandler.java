package parul.practice.minidynamo.transport;

import parul.practice.minidynamo.storage.StorageEngine;

public class WriteReplicaHandler implements RequestHandler{
    private final StorageEngine storageEngine;

    public WriteReplicaHandler(StorageEngine engine){
        this.storageEngine = engine;
    }
    @Override
    public void handle(Request request) {
        // Handle replica writes
    }
}
