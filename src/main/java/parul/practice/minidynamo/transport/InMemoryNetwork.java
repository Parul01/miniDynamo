package parul.practice.minidynamo.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryNetwork {
    private final Map<String, InMemoryTransport> transports =
            new ConcurrentHashMap<>();

    public void register(String nodeId, InMemoryTransport transport) {
        transports.put(nodeId, transport);
    }

    public InMemoryTransport getTransport(String nodeId) {
        return transports.get(nodeId);
    }
}
