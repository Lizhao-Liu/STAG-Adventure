package command;

import command.Where.Condition;
import command.common.NameValuePair;
import dbStructure.DatabaseManager;
import dbStructure.Row;
import dbStructure.Table;
import exception.CommandExecutionException;

import java.util.ArrayList;

public class Update extends CommandType{
    String tableName;
    ArrayList<NameValuePair> nameValuePairs;
    Condition condition;
    Table table;

    public Update()
    {
        tableName = "";
        nameValuePairs = new ArrayList<>();
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setNameValuePairs(ArrayList<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    //execute the update command in the given table
    @Override
    public void execute(DatabaseManager manager) throws  CommandExecutionException {
        table = manager.getCurrDB().getTable(tableName);
        ArrayList<Integer> targetRowIds = condition.getRowList(table);
        //walking through the rows which match the condition and update the values according to the name value pair
        for(int id : targetRowIds){
            Row row = table.getRowById(id);
            //walking through the name values pairs given by the query and update the value in turn
            for(NameValuePair pair:nameValuePairs){
                table.setRowValueByCol(row, pair.getColumnName(), pair.getValue());
            }
        }
    }
}
