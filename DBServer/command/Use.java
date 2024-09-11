package command;

import dbStructure.DatabaseManager;
import exception.CommandExecutionException;

public class Use extends CommandType{
    String dbName;
    public void setDbName(String name){
        this.dbName = name;
    }

    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        manager.useDB(dbName);
    }
}
