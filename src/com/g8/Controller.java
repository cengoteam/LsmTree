package com.g8;

import com.g8.query.QueryParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller {
	private QueryParser parser;
	public Controller(QueryParser parser){
		this.parser = parser;
	}

	public void run(){
		
	}
	public void getQuery() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please write a query: \n Exit (e)");
		String query = scanner.nextLine();
		while (!query.equals("e")) {
			System.out.println("Please write a query: \n Exit (e)");
			query = scanner.nextLine();
			parser.parser(query);

		}
		System.out.println("Program terminated.");
	}

	public void readScript() {
		File file = new File("./script.txt");

		try {
			Scanner scanner = new Scanner(file);

			//now read the file line by line...
			int lineNum = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				parser.parser(line);
			}
		} catch(FileNotFoundException e) {
			//handle this
		}
	}


}
