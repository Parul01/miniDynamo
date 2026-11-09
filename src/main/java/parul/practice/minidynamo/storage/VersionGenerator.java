package parul.practice.minidynamo.storage;

import java.util.concurrent.atomic.AtomicLong;

public class VersionGenerator {
    private AtomicLong counter;

    public VersionGenerator() {
        this.counter = new AtomicLong(0);
    }

    public long nextVersion(){
        return this.counter.incrementAndGet();
    }

    public void observe(long receivedCounter){
        counter.updateAndGet(current -> Math.max(current, receivedCounter));
    }

}
