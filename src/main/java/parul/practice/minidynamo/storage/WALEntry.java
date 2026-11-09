package parul.practice.minidynamo.storage;

public class WALEntry {

    private int txnId;
    private long seqNo;
    private OperationType operation;
    private String key;
    private VersionedValue value;
    private long checkSum;

    public WALEntry(Builder builder) {

        txnId = builder.txnId;
        seqNo = builder.seqNo;
        operation = builder.operation;
        key = builder.key;
        value = builder.value;
        checkSum = builder.checkSum;

    }

    public int getTxnId() {
        return txnId;
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
                "txnId='" + txnId + '\'' +
                ", seqNo='" + seqNo + '\'' +
                ", operation=" + operation +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", checkSum='" + checkSum + '\'' +
                '}';
    }

    public static class Builder{

        private int txnId;
        private long seqNo;
        private OperationType operation;
        private String key;
        private VersionedValue value;
        private long checkSum;

        public Builder setTxnId(int txnId) {
            this.txnId = txnId;
            return this;
        }

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
