package com.g8.ssTable;

import java.io.*;

import com.g8.model.IndexRecord;
import com.g8.model.Record;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileSeeker {

    public static void compactAndMerge(String f1, String f2, String f3){
        compactTwoFiles(f3, f2, "./tmp_segment.csv");
        new File(f3).delete();
        new File(f2).delete();
        if (new File("./segmentL1S.csv").exists()) {
            compactTwoFiles("./tmp_segment.csv", f1, "./tmp_segment2.csv");
            new File("./tmp_segment.csv").delete();
            new File(f1).delete();
            compactTwoFiles("./tmp_segment2.csv", "./segmentL1S.csv", "./tmp_segment3.csv");
            new File("./tmp_segment2.csv").delete();
            new File("./segmentL1S.csv").delete();
            new File("./tmp_segment3.csv").renameTo(new File("./segmentL1S.csv"));
        }else {
            compactTwoFiles("./tmp_segment.csv", f1, "./segmentL1S.csv");
            new File("./tmp_segment.csv").delete();
            new File(f1).delete();
        }
    }

    private static void compactTwoFiles(String f1, String f2, String w) {

        try {
            RandomAccessFile br1 = new RandomAccessFile(new File(f1), "r");
            RandomAccessFile br2 = new RandomAccessFile(new File(f2), "r");

            ArrayList<String> file1 = new ArrayList<>();
            ArrayList<String> file2 = new ArrayList<>();
            int indexStart = Integer.valueOf(br1.readLine().split(":")[1]);
            int indexLength = Integer.valueOf(br1.readLine().split(":")[1]);
            br1.seek(indexStart + indexLength);
            indexStart = Integer.valueOf(br2.readLine().split(":")[1]);
            indexLength = Integer.valueOf(br2.readLine().split(":")[1]);
            br2.seek(indexStart + indexLength);
            String line1 = br1.readLine();
            String line2 = br2.readLine();

            while (line1 != null) {
                file1.add(line1);
                line1 = br1.readLine();
            }

            while (line2 != null) {
                file2.add(line2);
                line2 = br2.readLine();
            }

            ArrayList<String> mergedList = (ArrayList<String>) file2.clone();
            for (String record1 : file1) {
                String key1 = record1.split(":")[0];
                for (String record2 : file2) {
                    String key2 = record2.split(":")[0];
                    if (key1.compareTo(key2) == 0) {
                        break;
                    } else if (key1.compareTo(key2) < 0) {
                        int index = file2.indexOf(record2);
                        mergedList.add(index, record1);
                    } else if (file2.indexOf(record2) == file2.size() - 1) {
                        mergedList.add(record1);
                    }

                }
            }
            writeData(w, mergedList);

            br1.close();
            br2.close();
        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }

    }

    public static List<IndexRecord> initSparseIndex(String fileName) throws IOException {

        RandomAccessFile file = new RandomAccessFile(new File(fileName), "r");

        List<IndexRecord> sparseIndex = new ArrayList<>();
        int indexStart = Integer.valueOf(file.readLine().split(":")[1]);
        int indexLength = Integer.valueOf(file.readLine().split(":")[1]);
        for (int i = indexStart; i < indexLength; i += 38) {
            String line = file.readLine();
            String lineKey = parseStringByte(line.split(":")[0]);
            Integer lineValue = parseIntegerByte(line.split(":")[1]);
            IndexRecord record = new IndexRecord(lineKey, lineValue);
            sparseIndex.add(record);
        }

        file.close();
        return sparseIndex;
    }

    public static void initSSTable(List<Record> memTable, String fileName) {
        ArrayList<String> list = new ArrayList<>();
        for (Record record : memTable) {
            String key = formatByte(record.getKey(), 30);
            String value = formatByte(record.getValue(), 10);
            String insertValue = key + ":" + value;
            list.add(insertValue);
        }
        writeData(fileName, list);
    }

    private static void writeData(String file, ArrayList<String> list) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String indexStartStr = formatByte(37, 5);
            bw.write("indexStart:" + indexStartStr);
            bw.newLine();
            String indexLengthStr;
            int indexSize;
            if (list.size() % 4 == 1 || list.size() % 4 == 0) {
                indexSize = list.size() / 4 + 1;
            } else {
                indexSize = list.size() / 4 + 2;
            }
            indexLengthStr = formatByte(indexSize * 38, 5);
            bw.write("indexLength:" + indexLengthStr);
            bw.newLine();

            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1 && i % 4 != 0) {
                    bw.write(list.get(i).split(":")[0] + ":");
                    bw.write(formatByte((i * 43) + (indexSize * 38 + 37), 5));
                    bw.newLine();
                } else if (i % 4 == 0) {
                    bw.write(list.get(i).split(":")[0] + ":");
                    bw.write(formatByte((i * 43) + (indexSize * 38 + 37), 5));
                    bw.newLine();
                }
            }

            for (String line : list) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }

    }

    public static String returnData(String fileName, int byteStart) {
        String line = null;
        try {
            RandomAccessFile file = new RandomAccessFile(new File(fileName), "r");
            file.seek(byteStart);
            line = file.readLine();

            String key = line.split(":")[0];
            String value = line.split(":")[1];
            String keyStr = parseStringByte(key);
            int valueInt = parseIntegerByte(value);
            line = keyStr+":"+valueInt;
            file.close();

        } catch (IOException e) {
            System.out.println("File not found");
            System.out.println("Exception: " + e.getMessage());
            System.exit(-1);
        }
        return line;
    }

    public static String returnData(String fileName, String key, int byteStart, int byteEnd) {
        String line = null;
        try {
            RandomAccessFile file = new RandomAccessFile(new File(fileName), "r");
            file.seek(byteStart);
            while (file.getFilePointer() != byteEnd) {
                line = file.readLine();

                String lineKey = line.split(":")[0];
                String value = line.split(":")[1];
                String keyStr = parseStringByte(lineKey);
                int valueInt = parseIntegerByte(value);
                line = keyStr+":"+valueInt;
                if (keyStr.equals(key)) {
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
            while (file.getFilePointer() != byteEnd+43) {
                String line = file.readLine();
                String key = line.split(":")[0];
                String value = line.split(":")[1];
                String keyStr = parseStringByte(key);
                int valueInt = parseIntegerByte(value);
                if (keyStr.compareToIgnoreCase(keyStart) >= 0 && keyStr.compareToIgnoreCase(keyEnd) <= 0){
                    list.add(keyStr+":"+valueInt);
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

    private static String formatByte(int number, int numberOfByte) {
        String str = String.valueOf(number);
        for (int i = 0; i < numberOfByte - String.valueOf(number).length(); i++) {
            str = "0" + str;
        }
        return str;
    }

    private static String formatByte(String name, int numberOfByte) {
        String str = name;
        for (int i = 0; i < numberOfByte - name.length(); i++) {
            str += "_";
        }
        return str;
    }

    private static String parseStringByte(String str){
        return str.split("_")[0];
    }

    private static Integer parseIntegerByte(String str){
        int number = 0;
        if (str.split("-").length > 1){ //negative
            number = -(Integer.valueOf(str.split("-")[1]));
        }else {
            number = Integer.valueOf(str.split("-")[0]);
        }
        return number;
    }
}
