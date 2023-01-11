package com.g8;

import com.g8.model.MovieRecord;
import com.g8.model.Record;

public class Main {

    public static void main(String[] args) {
        Record record = new MovieRecord();
        record.setKey("matrix");
        record.setValue(12);
        Record mobbyDickRecord = new MovieRecord("mobbyDick", 11);
        Record mobbyDickRecord1 = new MovieRecord("mobbyDickk", 13);
        Record mobbyDickRecord2 = new MovieRecord("mobbyDickkk", 12);
        Record mobbyDickRecord3 = new MovieRecord("mobbyDickkkk", 15);
        MemTable memTable = new MemTable();
        memTable.insertRecord(record);

        memTable.insertRecord(mobbyDickRecord);
        memTable.insertRecord(mobbyDickRecord1);
        memTable.insertRecord(mobbyDickRecord2);
        memTable.insertRecord(mobbyDickRecord3);
        memTable.deleteRecordByKey("matrix");
        System.out.println("1 " + memTable.searchByKey("mobbyDick"));
        memTable.print();

    }
}
