package command;

import dbStructure.DatabaseManager;
import dbStructure.Table;
import exception.CommandExecutionException;

public class Alter extends CommandType {
    private AlterationType type;
    private String columnName;
    private String tableName;
    Table table;

    public Alter(){}
    public enum AlterationType {
        ADD, DROP;
    }
    public void setTableName(String tableName){
        this.tableName = tableName;
    }
    public void setType(AlterationType type){
        this.type = type;
    }
    public void setColumnName(String columnName){
        this.columnName = columnName;
    }
    //execute the ALTER command on the target table
    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        table = manager.getCurrDB().getTable(tableName);
        if(type==AlterationType.ADD) table.addColumn(columnName);
        if(type==AlterationType.DROP) table.dropColumn(columnName);
    }

}
