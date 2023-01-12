package com.g8;

import com.g8.query.QueryExecuter;
import com.g8.query.QueryParser;
import com.g8.ssTable.TableList;

public class DBSimulator {
    public void run(){

        TableList tableList = new TableList();
        MemTable memTable = new MemTable(tableList);
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
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('a', 12);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('b', 12);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('c', 12);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('d', 12);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('e', 12);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('f', 12);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('g', 120);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('h', 250100);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('j', 250100);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('k', 32);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('l', 32);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('m', 32);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('b', 14);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('c', 50);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('g', 14);");
        parser.parser("INSERT INTO table_name (movieName, views) VALUES ('y', 50);");
        //memTable.print();
        parser.parser("DELETE FROM MOVIES WHERE movieName = 'c'");
        parser.parser("SELECT * FROM MOVIES WHERE movieName < 'd'");

    }
}
