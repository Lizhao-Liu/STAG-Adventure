package dbStructure;

import exception.CommandExecutionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    //the saving path of the databases;
    public static final String rootPath = "./database/";
    //the current database in use
    private Database currDB;
    //a record of the existing databases available
    private ArrayList<Database> databases;
    //a hashmap of the database name and its index which is set up for quickly locating the database index with the name provided
    private Map<String, Integer> databaseMap;

    public DatabaseManager()
    {
        File rootDir = new File(rootPath);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        databases = new ArrayList<>();
        databaseMap = new HashMap<>();
    }

    //set up the current working database
    public void useDB(String name) throws CommandExecutionException {
        if(!databaseMap.containsKey(name)) throw new CommandExecutionException("Database "+ name + " doesn't exist");
        this.currDB = databases.get(databaseMap.get(name));
    }

    //get the current using database back
    public Database getCurrDB() throws CommandExecutionException {
        if(currDB==null){
            throw new CommandExecutionException("please choose the database first");
        }
        return currDB;
    }

    //check if the given database exists
    public boolean containsDB(String name){
        if(!databaseMap.containsKey(name)) return false;
        return true;
    }

    //drop the given database with the command "DROP XXX"
    public void dropDatabase(String name) throws CommandExecutionException
    {
        String dbPath = rootPath+name+'/';
        File dir = new File(dbPath);
        if(dir.exists()){
            try {
                //delete the database folder recursively
                Path dirPath = Paths.get(dbPath);
                Files.walk(dirPath).map(Path::toFile)
                        .sorted(Comparator.comparing(File::isDirectory))
                        .forEach(File::delete);
            } catch(IOException e){
                throw new CommandExecutionException("Fail to delete the folder "+ name);
            }
        }
        //throw an error if the database already exists
        if(!databaseMap.containsKey(name)) throw new CommandExecutionException("Database "+ name + " doesn't exist");
        databases.remove(databaseMap.get(name));
        databaseMap.remove(name);
        //clear the currDB value if it points to the deleted database
        if(currDB!=null && currDB.getDbName().equals(name)){
            currDB=null;
        }
    }

    //create a new database
    public void addDatabase(String name) throws CommandExecutionException
    {
        if(containsDB(name)) throw new CommandExecutionException("Database "+ name + "already exits");
        Database db = new Database(name);
        databaseMap.put(name, databases.size());
        databases.add(db);
    }

}
