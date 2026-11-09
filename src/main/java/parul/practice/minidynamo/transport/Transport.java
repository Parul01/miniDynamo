package parul.practice.minidynamo.transport;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.transport.handler.RequestHandler;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface Transport {

    public CompletableFuture<Response> sendAsync(Node node, Request request);

    public void registerHandler(String action, RequestHandler handler);

    public Response receive(Request request) throws IOException;

}
