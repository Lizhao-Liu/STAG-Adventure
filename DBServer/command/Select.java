package command;

import command.Where.Condition;
import dbStructure.*;
import exception.CommandExecutionException;

import java.util.ArrayList;

public class Select extends CommandType{
    //check if all columns are selected
    boolean isSelectAllCols;
    //check if there is a condition provided in the query
    boolean hasWhere;
    ArrayList<String> columnList;
    String tableName;
    Table table;
    Condition condition;
    String output;

    public Select()
    {
        isSelectAllCols=false;
        hasWhere=false;
        columnList = new ArrayList<>();
    };

    public void setColumnList(ArrayList<String> columnList) {
        this.columnList = columnList;
    }

    public void setCondition(Condition condition){
        this.condition = condition;
        hasWhere=true;
    }

    public void setSelectAllCols() {
        isSelectAllCols = true;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        table = manager.getCurrDB().getTable(tableName);
        StringBuilder s = new StringBuilder();
        //case 1: select * from table xx;
        if(isSelectAllCols&&!hasWhere){
            allColsWithCond(s);
        }
        //case 2: select <attribute list> from table xxx;
        if(!isSelectAllCols&&!hasWhere){
            ArrayList<Integer> resCols = new ArrayList<>();
            allColsWithNoCond(resCols ,s);
        }
        //case 3: select <attribute list> from table xxx where <condition>;
        if(!isSelectAllCols&&hasWhere){
            ArrayList<Integer> resRows = condition.getRowList(table);
            ArrayList<Integer> resCols = new ArrayList<>();
            lmtColsWithCond(resRows,resCols,s);
        }
        //case 4: select * from table xxx where <condition>;
        if(isSelectAllCols&&hasWhere){
            ArrayList<Integer> resRows = condition.getRowList(table);
            allColsWithCond(resRows, s);
        }
        output = s.toString();
    }

    private void allColsWithCond(StringBuilder s)
    {
        //select all columns in the table
        for(String col:table.getColNames()){
            s.append(col).append('\t');
        }
        s.append('\n');
        //select all rows in the table
        for(Row row: table.getRows()){
            s.append(row.toString());
            s.append('\n');
        }
    }

    private void allColsWithNoCond(ArrayList<Integer> resCols, StringBuilder s) throws CommandExecutionException
    {
        //select the chosen columns from the table
        for(String colName:columnList){
            s.append(colName).append('\t');
            resCols.add(table.getColumnIndex(colName));
        }
        s.append('\n');

        //select all rows in the table
        for(Row row: table.getRows()){
            for(int i : resCols){
                s.append(row.getValue(i).getContent()).append('\t');
            }
            s.append('\n');
        }
    }

    private void lmtColsWithCond(ArrayList<Integer> resRows, ArrayList<Integer> resCols, StringBuilder s) throws CommandExecutionException
    {
        //select the chosen columns from the table
        for(String colName:columnList){
            s.append(colName).append('\t');
            resCols.add(table.getColumnIndex(colName));
        }
        s.append('\n');
        //select the matching rows from the table
        for(int id: resRows){
            Row row = table.getRowById(id);
            for(int i: resCols){
                s.append(row.getValue(i).getContent()).append('\t');
            }
            s.append('\n');
        }
    }

    private void allColsWithCond(ArrayList<Integer> resRows, StringBuilder s) throws CommandExecutionException
    {
        //select all rows in the table
        for(String col:table.getColNames()){
            s.append(col).append('\t');
        }
        s.append('\n');
        //select the matching rows from the table
        for(int id: resRows){
            Row row = table.getRowById(id);
            s.append(row.toString());
            s.append('\n');
        }
    }
    @Override
    public String getOutput(){return output;}

}
