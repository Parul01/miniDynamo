package parul.practice.minidynamo.transport.handler;

import parul.practice.minidynamo.transport.Request;
import parul.practice.minidynamo.transport.Response;

import java.io.IOException;

public interface RequestHandler {

    public Response handle(Request request) throws IOException;
}
