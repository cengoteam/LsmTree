package com.g8.ssTable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileSeeker {
    public static String returnData(String fileName, int byteStart){
        String line = null;
        try {
            RandomAccessFile file = new RandomAccessFile(new File(fileName),"r");
            file.seek(byteStart);
            line = file.readLine();
            file.close();

        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        return line;
    }

    public static String returnData(String fileName, String key, int byteStart, int byteEnd){
        String line = null;
        try {
            RandomAccessFile file = new RandomAccessFile(new File(fileName),"r");
            file.seek(byteStart);
            while (file.getFilePointer() != byteEnd){
                line = file.readLine();
                String lineKey = line.split(":")[0];
                if (lineKey.equals(key)){
                    break;
                }
            }

            file.close();
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        return line;
    }

    public static List<String> returnRangeData(String fileName, int byteStart, int byteEnd, String keyStart, String keyEnd){
        List<String> list = new ArrayList<>();
        try {
            RandomAccessFile file = new RandomAccessFile(new File(fileName), "r");
            file.seek(byteStart);
            while (file.getFilePointer() != byteEnd) {
                String line = file.readLine();
                String key = line.split(":")[0];
                if (key.compareToIgnoreCase(keyStart) >= 0 && key.compareToIgnoreCase(keyEnd) <= 0){
                    list.add(line);
                }
            }

            file.close();
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        return list;
    }
}
