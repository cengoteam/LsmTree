package com.g8;

import com.g8.model.MovieRecord;
import com.g8.rebBlackTree.RedBlackTree;
import com.g8.model.Record;
import com.g8.ssTable.FileSeeker;
import com.g8.ssTable.TableList;

import java.util.List;

public class MemTable {
    private RedBlackTree memTree;
    private TableList ssTables;
    private int size  = 0;
    private static final int MAX_SIZE = 12;
    public  MemTable(TableList ssTables){
        
        this.ssTables = ssTables;
        memTree = new RedBlackTree();
    }

    public void insertRecord(Record record){
        memTree.insertNode(record);
        size ++;
        if(size == MAX_SIZE){
            writeDisk();
        }
    }

    public void deleteRecord(Record anotherRecord){
        anotherRecord.setValue(-1);
        insertRecord(anotherRecord);
    }

    public List<Record> searchByRange(String startKey, String endKey){

        return memTree.searchByKeyRange(startKey,endKey);
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

    public void writeDisk(){
        int nextSegmentNumber = ssTables.getNextSegmentNumber();
        String fileName = "./L0S" + nextSegmentNumber + ".csv";
        FileSeeker.initSSTable(memTree.getTreeAsList(), fileName);
        ssTables.addSegmentFile(fileName);
        memTree = new RedBlackTree();
        size = 0;
    }
}
