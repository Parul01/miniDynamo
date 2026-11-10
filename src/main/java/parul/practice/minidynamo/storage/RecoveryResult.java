package parul.practice.minidynamo.storage;

import java.util.List;

public record RecoveryResult(
        long maxSeqNo,
        long validBytes,
        List<WALEntry> entries
) {
    @Override
    public long validBytes() {
        return validBytes;
    }
}