package dbStructure;

import exception.CommandExecutionException;

import java.io.File;
import java.util.*;

public class Database {
    private static final String rootDir = "./database/";
    private String dbName;
    private String dbPath;
    private ArrayList<Table> tables;
    private Map<String, Integer> tableMap;
    private static final String tableExtension = ".tb";

    public Database(String name)
    {
        this.dbName = name;
        this.dbPath = rootDir + dbName + '/';
        File dbDir = new File(this.dbPath);
        if (!dbDir.exists()) {
            dbDir.mkdir();
        }
        tableMap = new HashMap<>();
        tables = new ArrayList<>();
    }

    //check if the database contains the table with the name given
    public boolean containsTable(String tableName){
        if(!tableMap.containsKey(tableName)) return false;
        return true;
    }

    //add a table to the current database
    public Table addTable(String name) throws CommandExecutionException
    {
        if(containsTable(name)) throw new CommandExecutionException("table " + name + " already exists");
        Table tb = new Table(name);
        tb.setDatabase(this);
        tb.setPath(dbPath + name + tableExtension);
        tb.create();
        tableMap.put(name, tables.size());
        tables.add(tb);
        return tb;
    }

    //get the table back with the name given
    public Table getTable(String tableName) throws CommandExecutionException
    {
        if(!tableMap.containsKey(tableName)) throw new CommandExecutionException("Table "+ tableName +" doesn't exist");
        return tables.get(tableMap.get(tableName));
    }

    //drop the table in this database
    public void dropTable(String tableName) throws CommandExecutionException
    {
        File target = new File(dbPath+tableName+".tb");
        if(target.exists()){
            if(!target.delete()) throw new CommandExecutionException("Fail to delete Table "+ tableName+"due to i/o issues");
        }
        if(!containsTable(tableName)) throw new CommandExecutionException("Table " + tableName +" doesn't exist");
        tables.remove(tables.get(tableMap.get(tableName)));
        tableMap.remove(tableName);
    }

    public String getDbName(){
        return dbName;
    }
}
