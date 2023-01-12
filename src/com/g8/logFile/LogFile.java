package com.g8.logFile;

import java.io.File;
import java.util.List;

public class LogFile {

	public static boolean isFileExist(){
		return new File("./logs.txt").exists();
	}

	public static List<Record> readFile(){

	}
}
