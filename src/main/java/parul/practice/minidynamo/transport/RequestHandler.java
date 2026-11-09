package parul.practice.minidynamo.transport;

import java.io.IOException;

public interface RequestHandler {

    public Response handle(Request request) throws IOException;
}
