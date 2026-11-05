package parul.practice.minidynamo.transport;

import com.sun.net.httpserver.Request;
import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.cluster.Response;

import java.util.concurrent.CompletableFuture;

public class InMemoryTransport implements Transport {


    @Override
    public CompletableFuture<Response> sendAsync(Node node, Request request) {
        return null;
    }

    publc
}
