package com.g8;

import com.g8.model.MovieRecord;
import com.g8.rebBlackTree.RedBlackTree;
import com.g8.model.Record;
public class MemTable {
    private RedBlackTree memTree;
    private int size  = 0;
    public  MemTable(){
        memTree = new RedBlackTree();
    }

    public void insertRecord(Record record){
        memTree.insertNode(record);
        size ++;
        if(size == 12){
            writeDisk();
        }
    }

    public void deleteRecord(Record anotherRecord){
        anotherRecord.setValue(-1);
        insertRecord(anotherRecord);
    }

    public void deleteRecordByKey(String key){
        Record recordToBeDeleted = new MovieRecord(key,-1);
        insertRecord(recordToBeDeleted);
    }

    public Record searchByKey(String key){
        return memTree.searchRecordByKey(key);

    }
    public void print(){
        System.out.println(memTree);
    }

    public void getRecordBy(){

    }

    private void writeDisk(){

    }
}
