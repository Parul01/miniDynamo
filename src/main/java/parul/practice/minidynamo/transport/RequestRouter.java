package parul.practice.minidynamo.transport;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.coordinator.Coordinator;
import parul.practice.minidynamo.ring.ConsistentHashingRing;
import parul.practice.minidynamo.transport.request.ReadRequest;
import parul.practice.minidynamo.transport.request.WriteRequest;

import java.io.IOException;
import java.util.List;

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

    public void put(Request request) throws IOException {
        WriteRequest writeRequest = (WriteRequest) request;
        String key = writeRequest.getKey();
        String value = writeRequest.getValue();
        if(getFirstHealthyNode(key).getId().equals(this.nodeId)){
            coordinator.put(key, value);
        }else{
            // send to tarsnport
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
