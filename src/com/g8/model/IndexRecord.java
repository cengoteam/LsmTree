package com.g8.model;

public class IndexRecord {
    public IndexRecord(String key, int offset) {
        this.key = key;
        this.offset = offset;
    }

    private String key;
    private int offset;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
