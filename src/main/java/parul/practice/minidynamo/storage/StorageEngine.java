package parul.practice.minidynamo.storage;

import parul.practice.minidynamo.storage.walService.WALService;

import java.io.IOException;
import java.util.Optional;

public class StorageEngine {

    WALService walService;

    public StorageEngine(WALService walService){
        this.walService = walService;
    }

    public boolean put(String key, VersionedValue value) throws IOException {
        // all put requests to db will go from here, we need synchronized block for compare and set operation to our counter object
        boolean flag = false;
        synchronized (this){
            Optional<VersionedValue> existing = walService.get(key);
            if(!existing.isPresent() || value.isNewerThan(existing.get())){
                flag = true;
                walService.put(key, value);
            }
        }
        return flag;
    }

    public Optional<VersionedValue> get(String key){
        return walService.get(key);
    }


}
