package command;

import command.Where.Condition;
import dbStructure.DatabaseManager;
import dbStructure.Row;
import dbStructure.Table;
import exception.CommandExecutionException;

import java.util.ArrayList;

public class Delete extends CommandType{
    String tableName;
    Table table;
    Condition condition;
    public Delete(){}
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        output= "";
        //get the table with the name provided
        table = manager.getCurrDB().getTable(tableName);
        //get the list of rows that match the condition
        ArrayList<Integer> deletedRowIds = condition.getRowList(table);
        //walking through the returned row list and delete each row from the table
        for(int id: deletedRowIds){
            table.deleteARow(id);
        }
    }
    public String getOutput(){return output;}
}
