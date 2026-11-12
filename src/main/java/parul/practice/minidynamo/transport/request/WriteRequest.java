package parul.practice.minidynamo.transport.request;

public class WriteRequest implements Request {
    private final String key;
    private final String value;

    public WriteRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String action() {
        return "client/write";
    }

}
