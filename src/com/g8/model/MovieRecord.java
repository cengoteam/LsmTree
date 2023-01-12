package com.g8.model;

import java.util.Objects;

public class MovieRecord  implements Record{
    private String key;
    private int value;

    public MovieRecord(){

    }

    public MovieRecord(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public MovieRecord(String input){
        String[] inputs = input.split(":");
        key = inputs[0];
        value = Integer.parseInt(inputs[1]);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareByKey(Record otherRecord){
       return this.key.compareToIgnoreCase(otherRecord.getKey()) ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieRecord record = (MovieRecord) o;
        return Objects.equals(key, record.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "MovieRecord{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
