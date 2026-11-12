package parul.practice.minidynamo.transport.response;

import parul.practice.minidynamo.storage.VersionedValue;
import parul.practice.minidynamo.transport.handler.ReplicaStatus;

public class ReadReplicaResponse implements Response {
    private final ReplicaStatus status;
    private final VersionedValue value;

    public ReadReplicaResponse(ReplicaStatus status, VersionedValue value) {
        this.status = status;
        this.value = value;
    }

    public VersionedValue getValue() {
        return value;
    }

    @Override
    public ReplicaStatus getStatus() {
        return status;
    }
}
