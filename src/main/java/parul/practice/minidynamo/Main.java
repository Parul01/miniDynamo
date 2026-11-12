package parul.practice.minidynamo;

import parul.practice.minidynamo.bootstrap.MiniDynamoCluster;
import parul.practice.minidynamo.server.MiniDynamoServer;
import parul.practice.minidynamo.storage.VersionedValue;
import parul.practice.minidynamo.storage.walService.WALService;
import parul.practice.minidynamo.transport.network.InMemoryNetwork;
import parul.practice.minidynamo.transport.request.WriteRequest;
import parul.practice.minidynamo.transport.response.WriteResponse;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        MiniDynamoCluster miniDynamoCluster = new MiniDynamoCluster();
        WriteRequest request = new WriteRequest("name", "Tom");
        InMemoryNetwork inMemoryNetwork = miniDynamoCluster.getNetwork();
        WriteResponse response = (WriteResponse) inMemoryNetwork.getTransport("node-1").receive(request);
        System.out.println(response.getStatus());


    }
}