package parul.practice.minidynamo.storage;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.CRC32;

// send to WALWriter to write to WAL
//replay from Reader
// do actual transactions
public class WALService {

    private int currentTxnId;          // active transaction
    private AtomicLong globalSeqNo;    // global sequence counter
    private WALWriter walWriter;       // writes to file
    private KVStore kvStore;           // in-memory DB
    private WALReader walReader;       // for recovery on startup
    private final String walPath;
    /*
    data/nodeA/wal.log
     */
    public WALService(String path) throws IOException {
        this.walPath = path;
        this.walWriter = new WALWriter(walPath);
        this.walReader = new WALReader(walPath);

        this.kvStore = new KVStore();

        globalSeqNo = new AtomicLong(0);
        currentTxnId = 0;
        startup();
    }

    // TODO: need to make it synchronized
    public void put(String key, VersionedValue value) throws IOException {

        long seqNo = globalSeqNo.incrementAndGet();
        long checksum = computeCheckSum(currentTxnId + "|" + seqNo + "|" +
                OperationType.PUT.name() + "|" + key + "|" + value);
        WALEntry entry = new WALEntry.Builder()
                .setSeqNo(seqNo)
                .setTxnId(currentTxnId)
                .setOperation(OperationType.PUT)
                .setKey(key)
                .setValue(value)
                .setCheckSum(checksum).build();

        walWriter.append(entry);

        // write to db
        kvStore.put(key, value); // db is concurrent hashmap so no need of synchronized block
    }

    // creates new txnId
    public void beginTransaction() {
       currentTxnId++;
    }

    public void delete(String key) throws IOException {
        long seqNo = globalSeqNo.incrementAndGet();
        long checksum = computeCheckSum(currentTxnId + "|" + seqNo + "|" +
                OperationType.DELETE.name() + "|" + key + "|" + "");
        WALEntry entry = new WALEntry.Builder()
                .setSeqNo(seqNo)
                .setTxnId(currentTxnId)
                .setOperation(OperationType.DELETE)
                .setKey(key)
                .setValue("")
                .setCheckSum(checksum).build();

        walWriter.append(entry);

        // write to db
        kvStore.remove(key);
    } // writes to WAL + KVStore

    public void commit() throws IOException {
        long seqNo = globalSeqNo.incrementAndGet();
        long checksum = computeCheckSum(currentTxnId + "|" + seqNo + "|" +
                OperationType.COMMIT.name() + "|" + "" + "|" + "");
        WALEntry entry = new WALEntry.Builder()
                .setSeqNo(seqNo)
                .setTxnId(currentTxnId)
                .setOperation(OperationType.COMMIT)
                .setCheckSum(checksum).build();

        walWriter.append(entry);

    }// writes COMMIT to WAL

    public void startup() throws IOException {

        RecoveryResult result = walReader.replay();
        try (FileChannel ch = FileChannel.open(Paths.get(walPath), StandardOpenOption.WRITE)) {
            ch.truncate(result.validBytes());
        }

        currentTxnId = result.firstValue();
        globalSeqNo.set(result.secondValue());
        List<WALEntry> validTxns = result.entries();

        for (WALEntry w: validTxns){
            if(w.getOperation().equals(OperationType.DELETE)){
                kvStore.remove(w.getKey());
            }else if(w.getOperation().equals(OperationType.PUT)){
                kvStore.put(w.getKey(), w.getValue());
            }else{
                System.out.println("Not a valid operation");
            }
        }



    } // called on restart — replays WAL into KVStore

    private long computeCheckSum(String data){
        CRC32 crc = new CRC32();
        crc.update(data.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

    public KVStore getKVStore() {
        return kvStore;
    }

    public VersionedValue get(String key){
        return kvStore.get(key);
    }
}
