package parul.practice.minidynamo.transport.response;

import parul.practice.minidynamo.transport.handler.ReplicaStatus;

public class WriteResponse implements Response{
    private final ReplicaStatus status;

    public WriteResponse(ReplicaStatus status) {
        this.status = status;
    }

    @Override
    public ReplicaStatus getStatus() {
        return status;
    }
}
