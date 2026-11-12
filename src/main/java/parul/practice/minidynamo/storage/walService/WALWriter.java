package parul.practice.minidynamo.storage.walService;

import parul.practice.minidynamo.storage.VersionedValue;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/*
WAL writer write WAL write ahead log  to the text file

 */
public class WALWriter {
    private final FileOutputStream fos;
    private BufferedWriter bw;

    public WALWriter(String path) throws IOException {
        this.fos = new FileOutputStream(path, true);
        this.bw  = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
    }

    private String escape(String value){
        if(value == null) return "";
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }


    public synchronized void append(WALEntry entry) throws IOException {
        VersionedValue value = entry.getValue();

        bw.write(entry.getSeqNo() + "|");
        bw.write(entry.getOperation().name());
        bw.write("|");
        bw.write(escape(entry.getKey()));
        bw.write("|");

        if (value != null) {
            bw.write(escape(value.getValue()));
        }
        bw.write("|");

        if (value != null) {
            bw.write(String.valueOf(value.getCounter()));
        }
        bw.write("|");

        if (value != null) {
            bw.write(escape(value.getNodeId()));
        }
        bw.write("|");

        bw.write(String.valueOf(entry.getCheckSum()));

        bw.newLine();
        bw.flush();


        fos.getChannel().force(true);

    }

    public void close() throws IOException {
        bw.close();
    }

}
