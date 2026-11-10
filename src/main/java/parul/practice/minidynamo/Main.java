package parul.practice.minidynamo;

import parul.practice.minidynamo.storage.VersionedValue;
import parul.practice.minidynamo.storage.WALService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        WALService walService = new WALService("wal.log");
        VersionedValue v0 = new VersionedValue("Alex", 1, "node-1");
        VersionedValue v1 = new VersionedValue("13", 1, "node-1");
        VersionedValue v2 = new VersionedValue("Delhi", 1, "node-1");
        VersionedValue v3 = new VersionedValue("Alex", 2, "node-1");


        walService.put("name", v0);
        walService.put("age", v1);
        walService.put("location", v2);
        walService.put("name", v3);
        walService.delete("name");
        System.out.println("Before restart:");
        System.out.println(walService.get("age"));
        System.out.println(walService.get("location"));
        System.out.println(walService.get("name"));

        walService.close();

        WALService recovered = new WALService("wal.log");

        System.out.println("\nAfter restart:");
        System.out.println(recovered.get("age"));
        System.out.println(recovered.get("location"));
        System.out.println(recovered.get("name"));

        recovered.close();



    }
}