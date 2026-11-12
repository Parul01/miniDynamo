package parul.practice.minidynamo.server;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.coordinator.Coordinator;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.storage.VersionGenerator;
import parul.practice.minidynamo.transport.handler.ReadHandler;
import parul.practice.minidynamo.transport.handler.ReadReplicaHandler;
import parul.practice.minidynamo.transport.handler.WriteHandler;
import parul.practice.minidynamo.transport.router.RequestRouter;
import parul.practice.minidynamo.transport.network.Transport;
import parul.practice.minidynamo.transport.handler.WriteReplicaHandler;

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


        this.initHandlers();

    }

    public RequestRouter getRequestRouter() {
        return requestRouter;
    }

    private void initHandlers(){
        transport.registerHandler(
                "replica/write",
                new WriteReplicaHandler(
                        storageEngine,
                        node,
                        versionGenerator
                )
        );
        transport.registerHandler(
                "replica/read",
                new ReadReplicaHandler(storageEngine)
        );
        transport.registerHandler(
                "client/write",
                new WriteHandler(requestRouter)
        );
        transport.registerHandler(
                "client/read",
                new ReadHandler(requestRouter)

        );
    }

}
