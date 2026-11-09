package parul.practice.minidynamo.transport;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.transport.request.WriteReplicaRequest;

import java.io.IOException;

public class WriteReplicaHandler implements RequestHandler{
    private final StorageEngine storageEngine;
    private final Node node;
    // so we craete these objects during initialization
    // transport calls init method when we create transport or we can call it later

    public WriteReplicaHandler(StorageEngine engine, Node node){

        this.storageEngine = engine;
        this.node = node;
    }
    @Override
    public Response handle(Request request) throws IOException {
        // Handle replica writes
        WriteReplicaRequest writeReplicaRequest = (WriteReplicaRequest) request;
        // compare and set

        node.setCounter(writeReplicaRequest.getVersionedValue().getCounter());
        boolean flag = storageEngine.put(writeReplicaRequest.getKey(), writeReplicaRequest.getVersionedValue());
        WriteReplicaResponse response;
        if(flag) return new WriteReplicaResponse(ReplicaWriteStatus.SUCCEEDED);
        return new WriteReplicaResponse(ReplicaWriteStatus.FAILED);



    }
}
