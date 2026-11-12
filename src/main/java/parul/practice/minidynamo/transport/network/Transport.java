package parul.practice.minidynamo.transport.network;

import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.transport.handler.RequestHandler;
import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.response.Response;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface Transport {

    public CompletableFuture<Response> sendAsync(Node node, Request request);

    public void registerHandler(String action, RequestHandler handler);

    public Response receive(Request request) throws IOException;

}
