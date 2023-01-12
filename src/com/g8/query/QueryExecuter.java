package com.g8.query;

import com.g8.MemTable;
import com.g8.model.Record;
import com.g8.model.ResultOfIndexSearch;
import com.g8.ssTable.TableList;

import java.util.*;

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
        Set<Record> records = new HashSet<>();
        if(memTableResults != null){
            records.addAll(memTableResults);
        }

        // now look ss tables
        List<Record> ssTableResults = ssTables.searchByRange(lowKey, highKey);
        if(ssTableResults != null){
            records.addAll(ssTableResults);
        }

        return records.size() == 0 ? new ArrayList<>() : new ArrayList<>(records);


    }
}
