package command;

import dbStructure.Column;
import dbStructure.DatabaseManager;
import dbStructure.Table;
import exception.CommandExecutionException;

import java.util.ArrayList;

public class CreateTable extends CommandType{

    String tableName;
    ArrayList<Column> attrList;
    boolean hasAttrList;
    public CreateTable(){hasAttrList = false;attrList = new ArrayList<>();}
    public void setTableName(String name){
        this.tableName = name;
    }
    //converting a list of column names to a list of column objects
    public void setAttrList(ArrayList<String> list)
    {
        for(int i=0 ; i < list.size(); i++){
            hasAttrList = true;
            Column column = new Column(list.get(i));
            attrList.add(column);
        }
    }
    @Override
    public void execute(DatabaseManager manager) throws CommandExecutionException {
        //making sure the user choose a database before with the "USE XXX" command
        if(manager.getCurrDB()==null) throw new CommandExecutionException("please choose a database to work on at first");
        //add the created table to the current working database
        Table tb = manager.getCurrDB().addTable(tableName);
        //if there is a attribute list provided, set the columns of the table
        if(hasAttrList){
            tb.setColumns(attrList);
        }
    }
}
