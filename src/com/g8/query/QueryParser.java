package com.g8.query;

import com.g8.model.Condition;
import com.g8.model.MovieRecord;
import com.g8.model.Record;
import com.g8.model.SelectCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    private QueryExecuter executer;

    public QueryParser(QueryExecuter executer){
        this.executer = executer;
    }
    /*INSERT INTO table_name (column1, column2, column3, ...)
    VALUES (value1, value2, value3, ...);*/
    public  void parser(String queryString) {
        String movieName = "";
        String[] parsedQuery = queryString.split(" ");
        if (parsedQuery[0].equals("SELECT")) {
            String tableName = "";
            String[] queryElements = queryString.split(" ");
            String columns = "";
            String table = "";
            String whereClause = "";


            for (int i = 1; i < queryElements.length; i++) {
                if (queryElements[i].equalsIgnoreCase("FROM")) {
                    for (int j = 1; j < i; j++) {
                        columns += queryElements[j] + ",";
                    }
                    columns = columns.substring(0, columns.length() - 1);
                    table = queryElements[i + 1];
                }
                if (queryElements[i].equalsIgnoreCase("WHERE")) {
                    whereClause = queryString.substring(queryString.indexOf("WHERE") + 5);
                }
            }

            //Extracting values with more than one words
            String whereClauseValue = "";
            String pattern = "(\')(.*?)(\')";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(whereClause);
            while (m.find()){
                whereClauseValue = m.group(2);
            }




            String[] whereSplit = whereClause.split(" "); //where clause split
            int count = 1;
            SelectCommand selectCommand = new SelectCommand();
            for (int i = 0 ; i < whereSplit.length ; i++) {
                if (whereSplit[i].equals("=")) {

                    String[] b = whereClause.split("'");
                    selectCommand.addCondition(new Condition("E", b[count]));
                    count +=2;
                }
                else if (whereSplit[i].equals("<")) {
                    String[] b = whereClause.split("'");
                    selectCommand.addCondition(new Condition("K", b[count]));
                    count += 2;
                }
                else if (whereSplit[i].equals(">")) {
                    String[] b = whereClause.split("'");
                    selectCommand.addCondition(new Condition("B", b[count]));
                    count += 2;
                }
            }
            if(selectCommand.getConditions().size() == 2){
                Condition conditionOne =  selectCommand.getConditions().get(0);
                Condition conditionTwo = selectCommand.getConditions().get(1);
                if(conditionOne.getType().equalsIgnoreCase("K")){
                    executer.searchByRange(conditionTwo.getKey(), conditionOne.getKey()).forEach(System.out::println);
                }else{
                    executer.searchByRange(conditionOne.getKey(), conditionTwo.getKey()).forEach(System.out::println);
                }
            }else{
                String conditionType = selectCommand.getConditions().get(0).getType();
                if(conditionType.equalsIgnoreCase("E")){
                    System.out.println("record found: " + executer.searchByKey(selectCommand.getConditions().get(0).getKey()));
                }else if(conditionType.equalsIgnoreCase("K")){
                    executer.searchByRange(null, selectCommand.getConditions().get(0).getKey()).forEach(System.out::println);
                }else if(conditionType.equalsIgnoreCase("B")){
                    executer.searchByRange(selectCommand.getConditions().get(0).getKey(),null).forEach(System.out::println);
                }
            }

        }
        else if (parsedQuery[0].equals("INSERT")) {
            String tableName = "";
            parsedQuery[6].replace("(", "");


            for (int i = 6; i<parsedQuery.length-1; i++) {
                movieName += parsedQuery[i];
            }
            String left = movieName.replace("(", "");
            String right = left.replace(")", "");
            String finalMovieName = right.replace(",", "");
            finalMovieName = finalMovieName.replace("'","");

            String views = parsedQuery[parsedQuery.length-1];
            String viewsClean1 = views.replace(";", "");
            String viewsClean2 = viewsClean1.replace(")", "");
            int viewsInt = Integer.parseInt(viewsClean2);
            Record newRecord = new MovieRecord(finalMovieName, viewsInt);
            executer.upsertRecord(newRecord);

        }
        else if (parsedQuery[0].equals("DELETE")) {
            String tableName = "";
            List<String> whereValues = new ArrayList<>();

            // Regular expression to match the table name
            String tableRegex = "DELETE FROM ([a-zA-Z]+)";
            Pattern tablePattern = Pattern.compile(tableRegex);
            Matcher tableMatcher = tablePattern.matcher(queryString);

            // Regular expression to match the WHERE clause values
            String whereRegex = "WHERE (.*)";
            Pattern wherePattern = Pattern.compile(whereRegex);
            Matcher whereMatcher = wherePattern.matcher(queryString);

            // Extract the table name
            if (tableMatcher.find()) {
                tableName = tableMatcher.group(1);
            }

            // Extract the values from the WHERE clause
            if (whereMatcher.find()) {
                String[] whereValuesArray = whereMatcher.group(1).split("AND");
                for (String value : whereValuesArray) {
                    whereValues.add(value.trim());
                }
            }



            String stringWhere = whereValues.get(0); // where clause as a string
            String[] whereSplit = stringWhere.split(" "); //where clause split
            int count = 1;

            for (int i = 0 ; i < whereSplit.length ; i++) {
                if (whereSplit[i].equals("=")) {

                    String[] b = stringWhere.split("'");
                    executer.deleteRecord(b[count]);
                    count +=2;
                }
                else if (whereSplit[i].equals("<")) {
                    String[] b = stringWhere.split("'");
                    count += 2;
                }
                else if (whereSplit[i].equals(">")) {
                    String[] b = stringWhere.split("'");
                    count += 2;
                }
            }





        }


    }


}
