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
        Controller controller = new Controller(parser);
        controller.run();

    }
}
