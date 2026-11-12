package parul.practice.minidynamo.storage.walService;

import parul.practice.minidynamo.storage.OperationType;
import parul.practice.minidynamo.storage.VersionedValue;

public class WALEntry {

    private final long seqNo;
    private final OperationType operation;
    private final String key;
    private final VersionedValue value;
    private final long checkSum;

    public WALEntry(Builder builder) {

        seqNo = builder.seqNo;
        operation = builder.operation;
        key = builder.key;
        value = builder.value;
        checkSum = builder.checkSum;

    }


    public long getSeqNo() {
        return seqNo;
    }

    public OperationType getOperation() {
        return operation;
    }

    public String getKey() {
        return key;
    }

    public VersionedValue getValue() {
        return value;
    }

    public long getCheckSum() {
        return checkSum;
    }

    @Override
    public String toString() {
        return "WALEntry{" +
                ", seqNo='" + seqNo + '\'' +
                ", operation=" + operation +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", checkSum='" + checkSum + '\'' +
                '}';
    }

    public static class Builder{

        private long seqNo;
        private OperationType operation;
        private String key;
        private VersionedValue value;
        private long checkSum;

        public Builder setSeqNo(long seqNo) {
            this.seqNo = seqNo;
            return this;

        }

        public Builder setOperation(OperationType operation) {
            this.operation = operation;
            return this;

        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setValue(VersionedValue value) {
            this.value = value;
            return this;
        }

        public Builder setCheckSum(long checkSum) {
            this.checkSum = checkSum;
            return this;
        }

        public WALEntry build(){
            return new WALEntry(this);
        }

    }
}
