package parul.practice.minidynamo.storage;

import java.io.IOException;

public class StorageEngine {

    WALService walService;

    public StorageEngine(WALService walService){
        this.walService = walService;
    }

    public boolean put(String key, VersionedValue value) throws IOException {
        // all put requests to db will go from here, i.e why we need synchronized block
        boolean flag = false;
        synchronized (this){
            VersionedValue existing = walService.get(key);
            if(value.isNewerThan(existing)){
                flag = true;
                walService.put(key, value);
            }
        }
        return flag;
    }

    VersionedValue get(String key){
        return null;
    }


}
