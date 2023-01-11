package com.g8.model;

public interface Record {
    int compareByKey(Record otherRecord);
    String getKey();
    void setKey(String key);
    int getValue();
    void setValue(int value);
}
