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
		Scanner scanner = new Scanner(System.in);
		System.out.println("[1] Write a query");
		System.out.println("[2] Write a script path");
		System.out.println("[e] Exit");
		String choice = scanner.nextLine();
		if(choice.equals("1")){
			getQuery();
		} else if (choice.equals("2")) {
			readScript();
		}
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
		System.out.println("Please write script file name with .txt extension:");
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.nextLine();
		File file = new File("./"+ fileName);

		try {
			scanner = new Scanner(file);

			//now read the file line by line...
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				parser.parser(line);
			}
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.out.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
	}
}
