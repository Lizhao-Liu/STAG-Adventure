package dbStructure;

import exception.CommandExecutionException;
import command.common.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private File tbFile;
    private TableWriter tableWriter;
    private Database database;
    private String path;
    private String name;
    private ArrayList<Column> columns;
    private Map<String, Integer> columnMap;
    private ArrayList<Row> rows;
    //get the primary key(id) with the row provided
    private Map<Row, Integer> rowToIdMap;
    //get the row with the primary key(id) provided
    private Map<Integer, Row> idToRowMap;
    private int nextRowId;
    public static final String pkName = "id";

    public Table(String name) {
        this.name = name;
        columnMap = new HashMap<>();
        rowToIdMap = new HashMap<>();
        idToRowMap = new HashMap<>();
        columns= new ArrayList<>();
        rows = new ArrayList<>();
        setUpPk();
    }

    // create the file of the table if it doesn't exist
    public void create() throws CommandExecutionException {
        tbFile = new File(this.path);
        if (!tbFile.exists()) {
            try {
                tbFile.createNewFile();
            } catch (IOException e) {
                throw new CommandExecutionException("fail to create the table" + name + " due to I/O problem");
            }
        } else {
            throw new CommandExecutionException("please delete the existing files using drop command first");
        }
        tableWriter = new TableWriter();
    }

    public void setDatabase(Database db){
        this.database = db;
    }
    public void setPath(String path){ this.path = path;}
    public String getPath(){ return path;}

    //set up the primary key column with the name of "id" and the type of Integer
    public void setUpPk()
    {
        Column primary = new Column(pkName);
        primary.setType(Value.ValueType.IntegerLiteral);
        primary.setTable(this);
        primary.setIndex(0);
        columnMap.put(pkName, columns.size());
        columns.add(primary);
        nextRowId = 1;
    }

    //the following part includes functions that deal with columns in this table
    //get the column back with the name specified
    public Column getColumn(String columnName) throws CommandExecutionException
    {
        if(containsColumn(columnName)){
            return columns.get(columnMap.get(columnName));
        }
        throw new CommandExecutionException("column " + columnName + " doesn't exist");
    }
    //get the column index with the name specified;
    public Integer getColumnIndex(String columnName) throws CommandExecutionException
    {
        if(containsColumn(columnName)){
            return columnMap.get(columnName);
        }
        throw new CommandExecutionException("column " + columnName + " doesn't exist");
    }
    //get the column with the index specified
    public Column getColumnByIndex(int index) throws CommandExecutionException {
        if(index >= columns.size()) throw new CommandExecutionException("column at index " + index + " doesn't exist");
        return columns.get(index);
    }

    //add a column to the table
    public void addColumn(String name) throws CommandExecutionException {
        if(containsColumn(name)) throw new CommandExecutionException("column "+ name+" already exists");
        Column column = new Column(name);
        column.setTable(this);
        column.setIndex(columns.size());
        columnMap.put(name, columns.size());
        columns.add(column);
        tableWriter.writeToTable();
    }

    //add a list of columns to the table
    public void setColumns(ArrayList<Column> attrList) throws CommandExecutionException
    {
        for(int i=0; i<attrList.size(); i++){
            Column target = attrList.get(i);
            if(!containsColumn(target.getName())){
                columnMap.put(target.getName(), columns.size());
                columns.add(target);
            }
            else{
                throw new CommandExecutionException("column "+ target.getName()+" already exists");
            }
        }
        tableWriter.writeToTable();
    }

    //drop a column from the table
    public void dropColumn(String columnName) throws CommandExecutionException
    {
        if(!containsColumn(columnName)) throw new CommandExecutionException("Column "+ columnName + " doesn't exist");
        int index = columnMap.get(columnName);
        //remove the values under this column from each row of the table
        for(int i = 0; i<rows.size();i++){
            rows.get(i).removeValue(index);
        }
        columns.remove(index);
        columnMap.remove(columnName);
        tableWriter.writeToTable();
    }
    //check if the table contains the column with the name specified
    public boolean containsColumn(String columnName) {
        return columnMap.containsKey(columnName);
    }
    //return a list of column names of the table
    public ArrayList<String> getColNames(){
        ArrayList<String> names= new ArrayList<>();
        for(int i =0; i < columns.size(); i++){
            names.add(columns.get(i).getName());
        }
        return names;
    }
    //return a list of columns of the table
    public ArrayList<Column> getColumns(){
        return columns;
    }

    //the following functions are dealt with rows of the table
    //add a row to the table
    public void addaRow(ArrayList<Value> values) throws CommandExecutionException
    {
        //check if the number of values in the given list match the length of columns of the table
        if((values.size()+1)!=columns.size()) throw new CommandExecutionException("the length of value list " +
                "doesn't match the length of columns in this table");
        Row row;
        //the first insertion of values determines the types of the columns in this table
        if(nextRowId==1){
            setUpColTypes(values);
            row = new Row(this, nextRowId, values);
        }
        //add the values to the row and check the types of values and the corresponding columns match
        else{
            row = new Row(this, nextRowId);
            addRowValues(values, row);
        }
        //add the row to the rows array and the map for future lookup
        rows.add(row);
        rowToIdMap.put(row, nextRowId);
        idToRowMap.put(nextRowId, row);
        nextRowId++;
        //save the changed status to the file
        tableWriter.writeToTable();
    }

    //add the values to the row and check the types of values and the corresponding columns match
    public void addRowValues(ArrayList<Value> values, Row row) throws CommandExecutionException
    {
        for(int i=0; i<values.size();i++){
            //"+1" is for the primary key column
            Column column = getColumnByIndex(i+1);
            Value temp = values.get(i);
            if(column.getType()!=temp.getValueType()){
                throw new CommandExecutionException("type conflict: Column " + column.getName()+ " accepts " +
                        column.getType()+"; Value "+ temp.getContent()+" is of "+ temp.getValueType()+" type");
            }
            row.addValue(i+1, temp);
        }
    }

    //the first insertion of values determines the types of the columns in this table
    public void setUpColTypes(ArrayList<Value> values){
        for(int i=0; i<values.size(); i++){
            columns.get(i+1).setType(values.get(i).getValueType());
        }
    }

    //delete a row with the id specified from the table
    public void deleteARow(int id) throws CommandExecutionException {
        Row row = getRowById(id);
        row.delete();
        idToRowMap.remove(id);
        rows.remove(row);
        rowToIdMap.remove(row);
        tableWriter.writeToTable();
    }

    //get the row back with the id specified
    public Row getRowById(int id) throws CommandExecutionException {
        if(id<=0 || id>nextRowId) throw new CommandExecutionException("Row at id "+ id + " doesn't exist");
        return idToRowMap.get(id);
    }

    //get the list of rows in the table
    public ArrayList<Row> getRows(){
        return rows;
    }

    //set the specific row value in the specific column to a given value
    public void setRowValueByCol(Row row, String columnName, Value value) throws CommandExecutionException
    {
        Column column = getColumn(columnName);
        int index = getColumnIndex(columnName);
        //check if the type of the value matches the required type of the given column
        if(column.getType()!=null){
            if(column.getType()!=value.getValueType()){
                throw new CommandExecutionException("type conflict: Column " + columnName+ " accepts " +
                        column.getType()+"; Value "+ value.getContent()+" is of "+ value.getValueType()+" type");
            }
        }
        //if the type of the column is not defined, set it to the type of the value
        else{
            column.setType(value.getValueType());
        }
        row.setValue(index, value);
        tableWriter.writeToTable();
    }

    //an helper class which can write the data to the table file
    private class TableWriter {

        public TableWriter() {}
        //update the table status to the file
        public void writeToTable() throws CommandExecutionException {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(tbFile));
                StringBuilder s = new StringBuilder();
                //write the column names to the table file;
                for (int i = 0; i < getColNames().size(); i++) {
                    s.append(getColNames().get(i)).append("\t");
                }
                writer.write(s.toString());
                writer.write("\n");
                //write the rows to the table file
                StringBuilder row = new StringBuilder();
                for (int i = 0; i < getRows().size(); i++) {
                    row.append(getRows().get(i).toString());
                    row.append('\n');
                }
                writer.write(row.toString());
                writer.close();
            }catch(IOException e){
                throw new CommandExecutionException("fail to save the data to table due to i/o problems");
            }
        }

    }

}
