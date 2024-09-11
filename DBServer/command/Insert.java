package command;


import command.common.Value;
import dbStructure.DatabaseManager;
import dbStructure.Table;
import exception.CommandExecutionException;

import java.util.ArrayList;

public class Insert extends CommandType{
    String tableName;
    Table table;
    ArrayList<Value> valueList;
    public Insert(){
        tableName = "";
        valueList = new ArrayList<>();
    }
    public void setTableName(String name){
        tableName = name;
    }
    public void setValueList(ArrayList<Value> list){
        valueList = list;
    }
    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        table = manager.getCurrDB().getTable(tableName);
        //converting the value list to a row and insert the row to the table
        table.addaRow(valueList);
    }


}
