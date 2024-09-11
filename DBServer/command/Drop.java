package command;

import dbStructure.DatabaseManager;
import exception.CommandExecutionException;

public class Drop extends CommandType{

    private StructureType type;
    private String name;
    public Drop(){}
    public void setType(StructureType type){
        this.type = type;
    }
    public void setName(String name){
        this.name = name;
    }
    public enum StructureType{table, database}
    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        if(type == StructureType.database){
            manager.dropDatabase(name);
        }
        else{
            manager.getCurrDB().dropTable(name);
        }
    }
}
