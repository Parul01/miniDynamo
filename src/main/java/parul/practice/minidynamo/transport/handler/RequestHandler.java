package parul.practice.minidynamo.transport.handler;

import parul.practice.minidynamo.transport.request.Request;
import parul.practice.minidynamo.transport.response.Response;

import java.io.IOException;

public interface RequestHandler {

    public Response handle(Request request) throws IOException;
}
