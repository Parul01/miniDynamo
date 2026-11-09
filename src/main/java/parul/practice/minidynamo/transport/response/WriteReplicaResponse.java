package parul.practice.minidynamo.transport.response;

import parul.practice.minidynamo.transport.ReplicaStatus;
import parul.practice.minidynamo.transport.Response;

public class WriteReplicaResponse implements Response {


    private final ReplicaStatus status;


    public WriteReplicaResponse(ReplicaStatus status) {
        this.status = status;
    }

    public ReplicaStatus getStatus() {
        return status;
    }
}
