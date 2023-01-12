package com.g8.logFile;

import com.g8.model.MovieRecord;
import com.g8.model.Record;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogFile {

	public static boolean isFileExist(){
		return new File("./logs.txt").exists();
	}

	public static List<Record> readFile(){//bos array don
		ArrayList<Record> records = new ArrayList<>();
		try {
			RandomAccessFile file = new RandomAccessFile(new File("./logs.txt"), "r");
			String line = file.readLine();
			if (line != null){
				while (line != null) {
					Record record = new MovieRecord(line);
					records.add(record);
					line = file.readLine();
				}
			}
			file.close();
		} catch (IOException e) {
			System.out.println("File not found");
			System.out.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
		return records;
	}

	public static void writeFile(String log){
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter("./logs.txt", true));
			file.write(log);
			file.newLine();
			file.close();
		} catch (IOException e) {
			System.out.println("File not found");
			System.out.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public static void newFile(){
		new File("./logs.txt").delete();
		try {
			new File("./logs.txt").createNewFile();
		} catch (IOException e) {
			System.out.println("File can not created");
			System.out.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
	}
}
