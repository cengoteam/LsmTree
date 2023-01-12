package com.g8;

import com.g8.query.QueryExecuter;
import com.g8.query.QueryParser;
import com.g8.ssTable.TableList;

public class DBSimulator {
    public void run(){
        MemTable memTable = new MemTable();
        TableList tableList = new TableList();
        QueryExecuter executer = new QueryExecuter(memTable, tableList);
        QueryParser parser = new QueryParser(executer);
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('a', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('b', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('c', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('d', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('e', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('f', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('g', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('h', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('j', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('k', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('l', 25000);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('m', 25000);");
        //memTable.print();
        parser.parser("SELECT * FROM MOVIES WHERE movieName > 'c' AND movieName < 'g'");

    }
}
