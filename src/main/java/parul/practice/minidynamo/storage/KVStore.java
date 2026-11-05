package parul.practice.minidynamo.storage;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KVStore {

    Map<String, VersionedValue> DB;

    public KVStore(){
        DB = new ConcurrentHashMap<>();
    }

    public void put(String key,VersionedValue v){
        DB.put(key,v);
    }


    public void remove(String key){
        DB.remove(key);
    }

    public VersionedValue get(String key){
        return DB.get(key);
    }

    // use for testing
    public Map<String, VersionedValue> getAll(){
        return Collections.unmodifiableMap(DB);
    }


}
