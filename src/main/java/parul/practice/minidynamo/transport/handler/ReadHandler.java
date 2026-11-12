package parul.practice.minidynamo.transport.handler;

import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.response.Response;
import parul.practice.minidynamo.transport.router.RequestRouter;

import java.io.IOException;

public class ReadHandler implements RequestHandler{
    private final RequestRouter router;

    public ReadHandler(RequestRouter router) {
        this.router = router;
    }

    @Override
    public Response handle(Request request) throws IOException {
        return null;
    }
}
