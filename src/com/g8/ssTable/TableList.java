package com.g8.ssTable;

import com.g8.model.Record;
import com.g8.model.MovieRecord;
import com.g8.model.ResultOfIndexSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableList {
    private List<SSTable> levelOneTables = new ArrayList<>();
    private SSTable mainSegment = null;
    public TableList(){
        String fileName = "";
        for (int i = 1; i < 4; i++) {
            fileName = "./L0S" + i + ".csv";
            try {
                levelOneTables.add(new SSTable(fileName, FileSeeker.initSparseIndex(fileName)));
            } catch (IOException e) {
                break;
            }
            try {
                fileName = "./L1S" + ".csv";
                mainSegment = new SSTable(fileName, FileSeeker.initSparseIndex(fileName));
            } catch (IOException e) {
                mainSegment = null;
            }
        }
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
        if(startKey == null){
            startKey = "A";
        }
        if(endKey == null){
            endKey = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        }
        Set<Record> uniqueResults = new HashSet<>();
        ResultOfIndexSearch indexRange = new ResultOfIndexSearch();
        for(int i = levelOneTables.size()-1; i>=0; i--){
            indexRange = levelOneTables.get(i).findByRange(startKey, endKey);
            if(indexRange != null){
                List<String> lineResults = FileSeeker.returnRangeData(indexRange.getFileName(), indexRange.getStartOffset(), indexRange.getEndOffset(), startKey, endKey);
                lineResults.forEach(line -> {
                    uniqueResults.add(new MovieRecord(line));
                });
            }

        }
        if(mainSegment!=null){
            indexRange = mainSegment.findByRange(startKey, endKey);
            if(indexRange != null){
                List<String> lineResultsMain = FileSeeker.returnRangeData(indexRange.getFileName(), indexRange.getStartOffset(), indexRange.getEndOffset(), startKey, endKey);
                lineResultsMain.forEach(line ->{
                    uniqueResults.add(new MovieRecord(line));
                });
            }

        }

        List<Record> results = new ArrayList<>(uniqueResults);
        return results;
    }
    private ResultOfIndexSearch findOffsetByKey(String key){
        ResultOfIndexSearch result = null;
        SSTable levelOneTable = null;
        for (int i = levelOneTables.size() -1 ; i>=0;i--) {
            levelOneTable = levelOneTables.get(i);
            if ((result = levelOneTable.findByKey(key)) != null) {
                return result;
            }
        }
        // not found on level 1 look main segment
        if(mainSegment != null && (result = mainSegment.findByKey(key)) != null){
            return result;
        }
        // not found
        return null;
    }

    public int getNextSegmentNumber() {
        return levelOneTables.size() + 1;
    }

    public void addSegmentFile(String fileName) {
        SSTable newTable = null;
        try {
            newTable = new SSTable(fileName, FileSeeker.initSparseIndex(fileName));
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        levelOneTables.add(newTable);
        if(levelOneTables.size() == 3){
            FileSeeker.compactAndMerge(levelOneTables.get(0).getFileName()
                    ,levelOneTables.get(1).getFileName()
                    ,levelOneTables.get(2).getFileName());
            if(mainSegment == null){
                String newMainSegmentFileName = "./L1S.csv";
                try {
                    mainSegment = new SSTable(newMainSegmentFileName, FileSeeker.initSparseIndex(newMainSegmentFileName));
                } catch (IOException e) {
                    System.out.println("File not found");
                    System.out.println("Exception: " + e.getMessage());
                    System.exit(-1);
                }
            }
            emptyLevelOne();
        }



    }

    private void emptyLevelOne(){
        levelOneTables.clear();
    }
}
