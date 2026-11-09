package parul.practice.minidynamo.transport;


import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.transport.handler.RequestHandler;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public class InMemoryTransport implements Transport {

    private final Map<String, RequestHandler> handlers;
    private final Map<Node, Transport> transport;
    private final ExecutorService executor;

    public InMemoryTransport(){
        handlers = new ConcurrentHashMap<>();
        transport = new ConcurrentHashMap<>();
        executor = Executors.newFixedThreadPool(10);
    }
    @Override
    public CompletableFuture<Response> sendAsync(Node node, Request request) {
       return CompletableFuture.supplyAsync(() -> {

            Response response = null;
            try {
                response = transport.get(node).receive(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return response;
        }, executor);

    }

    public void registerHandler(String action, RequestHandler handler){
        handlers.put(action, handler);
    }


    public Response receive(Request request) throws IOException {
        RequestHandler handler = handlers.get(request.action());
        return handler.handle(request);
    }
}
