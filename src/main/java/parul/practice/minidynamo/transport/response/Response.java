package parul.practice.minidynamo.transport.response;

import parul.practice.minidynamo.transport.handler.ReplicaStatus;

;

public interface Response {
    public ReplicaStatus getStatus();
}
