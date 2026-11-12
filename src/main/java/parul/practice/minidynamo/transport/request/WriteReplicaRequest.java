package parul.practice.minidynamo.transport.request;

import parul.practice.minidynamo.storage.VersionedValue;

public class WriteReplicaRequest implements Request {

    private final String key;
    private final VersionedValue versionedValue;

    public WriteReplicaRequest(String key, VersionedValue versionedValue) {
        this.key = key;
        this.versionedValue = versionedValue;
    }

    public String getKey() {
        return key;
    }

    public VersionedValue getVersionedValue() {
        return versionedValue;
    }

    @Override
    public String action() {
        return "replica/write";
    }
}
