package parul.practice.minidynamo.transport.network;


import parul.practice.minidynamo.cluster.Node;
import parul.practice.minidynamo.transport.handler.RequestHandler;
import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.response.Response;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public class InMemoryTransport implements Transport {

    private final Map<String, RequestHandler> handlers;
    private final InMemoryNetwork network;
    private final ExecutorService executor;

    public InMemoryTransport(InMemoryNetwork network){
        handlers = new ConcurrentHashMap<>();
        this.network = network;
        executor = Executors.newFixedThreadPool(10);
    }
    @Override
    public CompletableFuture<Response> sendAsync(Node node, Request request) {
       return CompletableFuture.supplyAsync(() -> {

            Response response = null;
            try {
                response = network.getTransport(node.getId()).receive(request);
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
