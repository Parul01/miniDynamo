package parul.practice.minidynamo.transport.response;

import parul.practice.minidynamo.transport.handler.ReplicaStatus;

public class WriteReplicaResponse implements Response {


    private final ReplicaStatus status;


    public WriteReplicaResponse(ReplicaStatus status) {
        this.status = status;
    }

    public ReplicaStatus getStatus() {
        return status;
    }
}
