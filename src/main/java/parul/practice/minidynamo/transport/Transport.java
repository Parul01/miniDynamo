package parul.practice.minidynamo.transport;

import com.sun.net.httpserver.Request;
import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.cluster.Response;

import java.util.concurrent.CompletableFuture;

public interface Transport {

    public CompletableFuture<Response> sendAsync(Node node, Request request);

    public void registerHandlers(Map<String, >);

}
