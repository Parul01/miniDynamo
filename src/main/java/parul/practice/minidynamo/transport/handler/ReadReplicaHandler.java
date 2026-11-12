package parul.practice.minidynamo.transport.handler;

import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.response.Response;

public class ReadReplicaHandler implements RequestHandler {

    private final StorageEngine storageEngine;

    public ReadReplicaHandler(StorageEngine engine) {
        this.storageEngine = engine;
    }

    @Override
    public Response handle(Request request) {
        // handle replica reads
        return  null;

    }
}
