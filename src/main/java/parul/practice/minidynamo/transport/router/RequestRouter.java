package parul.practice.minidynamo.transport.router;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.coordinator.Coordinator;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.transport.network.Transport;
import parul.practice.minidynamo.transport.request.ReadRequest;
import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.request.WriteRequest;
import parul.practice.minidynamo.transport.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RequestRouter {
    private final String nodeId;
    private final Coordinator coordinator;
    private final Transport transport;
    private final ConsistentHashingRing consistentHashingRing;
    int N = 3;

    public RequestRouter(String nodeId, Coordinator coordinator, Transport transport, ConsistentHashingRing consistentHashingRing) {
        this.nodeId = nodeId;
        this.coordinator = coordinator;
        this.transport = transport;
        this.consistentHashingRing = consistentHashingRing;
    }

    public Response put(Request request) throws IOException {
        WriteRequest writeRequest = (WriteRequest) request;
        String key = writeRequest.getKey();
        String value = writeRequest.getValue();
        Node node = getFirstHealthyNode(key);
        if(node.getId().equals(this.nodeId)){
            return coordinator.put(key, value);
        }else{
            // send to tarsnport
            CompletableFuture<Response> future = transport.sendAsync(node, request);
            try {
                return future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Request forwarding interrupted", e);
            } catch (ExecutionException e) {
                throw new IOException("Request forwarding failed", e.getCause());
            }

        }

    }
    public void get(Request request){
        ReadRequest readRequest = (ReadRequest) request;
        String key = readRequest.getKey();
    }

    private Node getFirstHealthyNode(String key){
        // TODO: health check pending
        List<Node> nodes = consistentHashingRing.getNodes(key, N);
        // if current node is in the list - that becoems coordinator
        for(Node node: nodes){
            if(node.getId().equals(this.nodeId)) return node;
        }

        return nodes.get(0);
    }
}
