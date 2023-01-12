package com.g8.ssTable;

import com.g8.model.Record;
import com.g8.model.MovieRecord;
import com.g8.model.ResultOfIndexSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableList {
    private List<SSTable> levelOneTables;
    private SSTable mainSegment;
    public TableList(){

    }

    public TableList(List<SSTable> levelOneTables, SSTable mainSegment) {
        this.levelOneTables = levelOneTables;
        this.mainSegment = mainSegment;
    }

    public Record findByKey(String key){
        ResultOfIndexSearch result  = findOffsetByKey(key);
        if(result == null){
            return null;
        }
        if(result.isExactMatch()){
            return new MovieRecord(FileSeeker.returnData(result.getFileName(), result.getStartOffset()));
        }else{
            return new MovieRecord(FileSeeker.returnData(result.getFileName(), key, result.getStartOffset(), result.getEndOffset()));
        }
    }

    public List<Record> searchByRange(String startKey, String endKey){
        Set<Record> uniqueResults = new HashSet<>();
        ResultOfIndexSearch indexRange = new ResultOfIndexSearch();
        for(int i = 0; i<levelOneTables.size(); i++){
            indexRange = levelOneTables.get(i).findByRange(startKey, endKey);
            List<String> lineResults = FileSeeker.returnRangeData(indexRange.getFileName(), indexRange.getStartOffset(), indexRange.getEndOffset(), startKey, endKey);
            lineResults.forEach(line -> {
                uniqueResults.add(new MovieRecord(line));
            });
        }
        indexRange = mainSegment.findByRange(startKey, endKey);
        List<String> lineResultsMain = FileSeeker.returnRangeData(indexRange.getFileName(), indexRange.getStartOffset(), indexRange.getEndOffset(), startKey, endKey);
        lineResultsMain.forEach(line ->{
            uniqueResults.add(new MovieRecord(line));
        });
        List<Record> results = new ArrayList<>(uniqueResults);
        return results;
    }
    private ResultOfIndexSearch findOffsetByKey(String key){
        ResultOfIndexSearch result = null;
        for (SSTable levelOneTable : levelOneTables) {
            if ((result = levelOneTable.findByKey(key)) != null) {
                return result;
            }
        }
        // not found on level 1 look main segment
        if((result = mainSegment.findByKey(key)) != null){
            return result;
        }
        // not found
        return null;
    }
}
