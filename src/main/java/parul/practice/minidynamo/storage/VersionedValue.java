package parul.practice.minidynamo.storage;

public final class VersionedValue {
    private final String value;
    private final long counter;
    private final String nodeId;   // tie-breaker

    public VersionedValue(String value, long counter, String nodeId) {
        this.value = value;
        this.counter = counter;
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return "VersionedValue{" +
                "value='" + value + '\'' +
                ", counter=" + counter +
                ", nodeId='" + nodeId + '\'' +
                '}';
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getValue()  { return value; }
    public long getCounter()  { return counter; }

    public boolean isNewerThan(VersionedValue other) {
        if (other == null) return true;
        if (this.counter != other.counter) return this.counter > other.counter;
        return this.nodeId.compareTo(other.nodeId) > 0;   // equal counters: node id decides
    }

//    public synchronized boolean compareAndSet(VersionedValue other){
//        if(other == null) return true;
//        if(this.counter < other.counter){
//            this.
//        }
//    }




}
