package parul.practice.minidynamo.storage.walService;

import parul.practice.minidynamo.storage.OperationType;
import parul.practice.minidynamo.storage.RecoveryResult;
import parul.practice.minidynamo.storage.VersionedValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.CRC32;

public class WALReader {

    BufferedReader reader;

    public WALReader(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
    }

    private List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean escaped = false;

        for (char c : line.toCharArray()) {

            if (escaped) {
                current.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '|') {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        if (escaped) {
            throw new IllegalArgumentException("Invalid WAL entry: trailing escape");
        }

        fields.add(current.toString());

        return fields;
    }

    private long computeCheckSum(String data){
        CRC32 crc = new CRC32();
        crc.update(data.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

    private boolean isChecksumValid(List<String> writes) {

        String data =Long.parseLong(writes.get(0)) + "|"
                + OperationType.valueOf(writes.get(1)).name() + "|"
                + writes.get(2) + "|"
                + writes.get(3) + "|"
                + writes.get(4) + "|"
                + writes.get(5);

        long crc = computeCheckSum(data);


        return crc == Long.parseLong(writes.get(6));

    }


    public RecoveryResult replay() throws IOException {
        // read from reader
        List<WALEntry> walEntries = new ArrayList<>();

        RecoveryResult result = null;
        long seq = 0;
        long mxSeq = 0;
        long validBytes = 0;

        String line = "";

        while ((line = reader.readLine()) != null){
            List<String> writes = null;
            VersionedValue value = null;
            OperationType operation;

            try {
                writes = splitFields(line);
                if(writes.size() != 7) throw new IllegalArgumentException("bad field count");
                seq = Long.parseLong(writes.get(0));
                Long.parseLong(writes.get(6));
                operation = OperationType.valueOf(writes.get(1));
                if (operation == OperationType.PUT) {

                    if (writes.get(5).isEmpty()) {
                        throw new IllegalArgumentException("Missing node ID");
                    }

                    value = new VersionedValue(
                            writes.get(3),
                            Long.parseLong(writes.get(4)),
                            writes.get(5)
                    );

                } else if (operation == OperationType.DELETE) {

                    if (!writes.get(3).isEmpty()
                            || !writes.get(4).isEmpty()
                            || !writes.get(5).isEmpty()) {
                        throw new IllegalArgumentException(
                                "Unexpected version fields"
                        );
                    }

                } else {
                    throw new IllegalArgumentException(
                            "Unsupported WAL operation"
                    );
                }



            }catch (RuntimeException ex){
                System.err.println("Torn/corrupt WAL tail, stopping: " + line);
                break;
            }

            if (!isChecksumValid(writes)) {
                System.err.println("Checksum mismatch, stopping replay at: " + line);
                break;
            }

            mxSeq = Math.max(mxSeq, seq);

            validBytes += line.getBytes(StandardCharsets.UTF_8).length+1;


            WALEntry wal = new WALEntry.Builder()
                    .setSeqNo(seq)
                    .setOperation(operation)
                    .setKey(writes.get(2))
                    .setValue(value) // TODO: can be null
                    .build();
            walEntries.add(wal);
        }

        walEntries.sort(Comparator.comparing(WALEntry::getSeqNo));

        result = new RecoveryResult(mxSeq, validBytes, walEntries);

        return result;

    }

    public void close() throws IOException {
        reader.close();
    }



}
