package parul.practice.minidynamo.transport;

public class WriteReplicaResponse implements Response{


    private final ReplicaWriteStatus status;


    public WriteReplicaResponse(ReplicaWriteStatus status) {
        this.status = status;
    }

    public ReplicaWriteStatus getStatus() {
        return status;
    }
}
