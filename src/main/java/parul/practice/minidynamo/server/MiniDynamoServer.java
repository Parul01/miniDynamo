package parul.practice.minidynamo.server;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.coordinator.Coordinator;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.storage.VersionGenerator;
import parul.practice.minidynamo.transport.RequestRouter;
import parul.practice.minidynamo.transport.Transport;

public class MiniDynamoServer {
    private final Node node;
    private final StorageEngine storageEngine;
    private final Coordinator coordinator;
    private final RequestRouter requestRouter;
    private final Transport transport;
    private final ConsistentHashingRing ring;
    private final VersionGenerator versionGenerator;

    public MiniDynamoServer(Node node, StorageEngine storageEngine, Transport transport, ConsistentHashingRing ring) {
        this.node = node;
        this.storageEngine = storageEngine;
        this.transport = transport;
        this.ring = ring;
        this.versionGenerator = new VersionGenerator();
        this.coordinator = new Coordinator(storageEngine, transport, node.getId(), ring, this.versionGenerator);
        this.requestRouter = new RequestRouter(node.getId(),coordinator, transport, ring);

    }

}
