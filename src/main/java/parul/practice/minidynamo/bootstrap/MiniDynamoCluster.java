package parul.practice.minidynamo.bootstrap;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.server.MiniDynamoServer;
import parul.practice.minidynamo.storage.StorageEngine;
import parul.practice.minidynamo.storage.WALService;
import parul.practice.minidynamo.transport.InMemoryNetwork;
import parul.practice.minidynamo.transport.InMemoryTransport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MiniDynamoCluster {

    private final List<Node> nodes;
    private final ConsistentHashingRing consistentHashingRing;
    private final InMemoryNetwork network;
    private final List<MiniDynamoServer> servers;

    public MiniDynamoCluster() throws IOException {

        nodes = List.of(
                new Node("node-1"),
                new Node("node-2"),
                new Node("node-3"),
                new Node("node-4"),
                new Node("node-5")
        );

        consistentHashingRing = new ConsistentHashingRing();

        for (Node node : nodes) {
            consistentHashingRing.addNode(node);
        }

        network = new InMemoryNetwork();
        servers = new ArrayList<>();

        // Create and register all transports first.
        List<InMemoryTransport> transports = new ArrayList<>();

        for (Node node : nodes) {
            InMemoryTransport transport = new InMemoryTransport(network);
            network.register(node.getId(), transport);
            transports.add(transport);
        }

        // Create one server and one independent storage engine per node.
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            Path walDirectory = Path.of("data", node.getId());
            Files.createDirectories(walDirectory);

            WALService walService =
                    new WALService(walDirectory.resolve("wal.log").toString());
//
//            WALService walService =
//                    new WALService("data/" + node.getId() + "/wal.log");

            StorageEngine storageEngine = new StorageEngine(walService);

            MiniDynamoServer server = new MiniDynamoServer(
                    node,
                    storageEngine,
                    transports.get(i),
                    consistentHashingRing
            );

            servers.add(server);
        }

    }

    public List<MiniDynamoServer> getServers() {
        return servers;
    }

    public ConsistentHashingRing getRing() {
        return consistentHashingRing;
    }
}
