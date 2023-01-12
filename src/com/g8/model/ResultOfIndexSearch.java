package com.g8.model;

public class ResultOfIndexSearch {
    private int startOffset;
    private int endOffset;
    private String fileName;
    private boolean isExactMatch;

    public ResultOfIndexSearch(){

    }

    public ResultOfIndexSearch(int startOffset, int endOffset, boolean isExactMatch, String fileName) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.isExactMatch = isExactMatch;
        this.fileName = fileName;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public boolean isExactMatch() {
        return isExactMatch;
    }

    public void setExactMatch(boolean exactMatch) {
        isExactMatch = exactMatch;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
