package parul.practice.minidynamo.transport.handler;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.storage.VersionGenerator;
import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.response.Response;
import parul.practice.minidynamo.transport.request.WriteReplicaRequest;
import parul.practice.minidynamo.transport.response.WriteReplicaResponse;

import java.io.IOException;

public class WriteReplicaHandler implements RequestHandler {
    private final StorageEngine storageEngine;
    private final Node node;
    private final VersionGenerator versionGenerator;
    // so we craete these objects during initialization
    // transport calls init method when we create transport or we can call it later

    public WriteReplicaHandler(StorageEngine engine, Node node, VersionGenerator versionGenerator){

        this.storageEngine = engine;
        this.node = node;
        this.versionGenerator = versionGenerator;
    }
    @Override
    public Response handle(Request request) throws IOException {
        // Handle replica writes
        WriteReplicaRequest writeReplicaRequest = (WriteReplicaRequest) request;
        // compare and set

        versionGenerator.observe(writeReplicaRequest.getVersionedValue().getCounter());
        boolean flag = storageEngine.put(writeReplicaRequest.getKey(), writeReplicaRequest.getVersionedValue()); // is this the correct counter to send

        if(flag) return new WriteReplicaResponse(ReplicaStatus.SUCCEEDED);
        return new WriteReplicaResponse(ReplicaStatus.FAILED);



    }
}
