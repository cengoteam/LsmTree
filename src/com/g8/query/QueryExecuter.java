package com.g8.query;

import com.g8.MemTable;
import com.g8.model.Record;
import com.g8.model.ResultOfIndexSearch;
import com.g8.ssTable.TableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryExecuter {

    private TableList ssTables;
    private MemTable memTable;

    public QueryExecuter(MemTable memTable, TableList tableList){
        this.memTable = memTable;
        this.ssTables = tableList;

    }



    public void upsertRecord(Record record){
        memTable.insertRecord(record);

    }
    public void deleteRecord(String key){
        memTable.deleteRecordByKey(key);
    }
    public Record searchByKey(String key){
        Record memTableResult;
        if ((memTableResult = memTable.searchByKey(key)) != null){
            return memTableResult;
        }
        // not found in memTable look ssTables
        return ssTables.findByKey(key);
    }

    public List<Record> searchByRange(String lowKey, String highKey){
        List<Record> memTableResults = memTable.searchByRange(lowKey,highKey);
        List<Record> records = new ArrayList<>(memTableResults);
        // now look ss tables
        records.addAll(ssTables.searchByRange(lowKey, highKey));
        return records;


    }
}
