package com.g8.ssTable;

import com.g8.model.IndexRecord;
import com.g8.model.ResultOfIndexSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SSTable {
    private String fileName;
    private List<IndexRecord> index;

    public ResultOfIndexSearch findByRange(String start, String end){
        ResultOfIndexSearch res = new ResultOfIndexSearch();
        int startOffset = findFirstOffset(start);
        int endOffset = findLastOffset(end);
        if(endOffset == -1){
            return null;
        }
        if(startOffset == -1){
            return null;
        }
        res.setFileName(this.fileName);
        res.setExactMatch(false);
        res.setEndOffset(endOffset);
        res.setStartOffset(startOffset);
        return res;


    }

    public ResultOfIndexSearch findByKey(String key){
        ResultOfIndexSearch result = new ResultOfIndexSearch();
        for(int i= 0; i<index.size(); i++){
            IndexRecord currentRecord = index.get(i);
            int comparisonResult = currentRecord.getKey().compareToIgnoreCase(key);
            if(comparisonResult>0){
                // even the first index's key is greater than search key do no match
                return null;
            }
            if(comparisonResult == 0){
                // exact match
                result.setExactMatch(true);
                result.setFileName(this.fileName);
                result.setStartOffset(currentRecord.getOffset());
                result.setEndOffset(currentRecord.getOffset());
                break;
            }

            // comparison result is smaller than 0 which means we need to look next index record see if search key is in between
            if(i == index.size() - 1){
                // end of the file no match
                return null;
            }
            // if not end of the file look next index record see if the key between
            IndexRecord nextRecord = index.get(i+1);
            if(nextRecord.getKey().compareToIgnoreCase(key)>0){
                // key is in between these two indexes
                result.setFileName(this.fileName);
                result.setStartOffset(currentRecord.getOffset());
                result.setEndOffset(nextRecord.getOffset());
                result.setExactMatch(false);
                return result;
            }
        }
        return null;
    }

    private int findFirstOffset(String key){
        int result = -1;
        for(int i= 0; i<index.size(); i++){
            IndexRecord currentRecord = index.get(i);
            int comparisonResult = currentRecord.getKey().compareToIgnoreCase(key);
            if(comparisonResult>0){
                // even the first index's key is greater than search key do no match
                return index.get(index.size()).getOffset();
            }
            if(comparisonResult == 0){
                // exact match
                return currentRecord.getOffset();
            }

            // comparison result is smaller than 0 which means we need to look next index record see if search key is in between
            if(i == index.size() - 1){
                // end of the file no match
                return result;
            }
            // if not end of the file look next index record see if the key between
            IndexRecord nextRecord = index.get(i+1);
            if(nextRecord.getKey().compareToIgnoreCase(key)>0){
                // key is in between these two indexes
                return currentRecord.getOffset();
            }
        }
        return result;

    }

    private Integer findLastOffset(String key){
        int result = index.get(index.size()-1).getOffset();
        for(int i= index.size() - 1 ; i>=0; i--){
            IndexRecord currentRecord = index.get(i);
            int comparisonResult = currentRecord.getKey().compareToIgnoreCase(key);
            if(comparisonResult<0){
                // even the first index's key is smaller than search key all records match
                return result;
            }
            if(comparisonResult == 0){
                // exact match
                return currentRecord.getOffset();
            }

            if(i == 0){
                return -1;
            }

            // current index is larger than compared index look next index record see if the key between
            IndexRecord prevRecord = index.get(i-1);
            if(prevRecord.getKey().compareToIgnoreCase(key)<0){
                // key is in between these two indexes
                return currentRecord.getOffset();
            }
        }
        return -1;


    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<IndexRecord> getIndex() {
        return index;
    }

    public void setIndex(List<IndexRecord> index) {
        this.index = index;
    }
}
