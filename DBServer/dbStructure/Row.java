package dbStructure;

import exception.CommandExecutionException;
import command.common.Value;

import java.util.ArrayList;

public class Row {
    private ArrayList<Value> row;
    private Table table;
    public boolean isDeleted;
    int id;

    public Row(Table table, int id, ArrayList<Value> values)
    {
        this.table = table;
        this.id = id;
        this.isDeleted=false;
        row = new ArrayList<>();
        //set up primary key as the first element of the row;
        setUpPk();
        row.addAll(values);
    }

    public Row(Table table, int id)
    {
        this.table = table;
        this.id = id;
        this.isDeleted=false;
        row = new ArrayList<>();
        //set up primary key as the first element of the row;
        setUpPk();
    }

    public int getId(){
        return id;
    }

    //set up the primary key placeholder of the row
    public void setUpPk(){
        Value pk = new Value();
        pk.setValueType(Value.ValueType.IntegerLiteral);
        pk.setContent(Integer.toString(id));
        row.add(pk);
    }

    //set the value at the index of the row to a given value
    public void setValue(int index, Value value)
    {
        if(index >= row.size()) {
            row.add(index, value);
        }
        else{
            row.set(index, value);
        }
    }

    //add value to the row
    public void addValue(int columnIndex, Value value)
    {
        row.add(columnIndex, value);
    }

    //delete the row
    public void delete(){
        isDeleted = true;
    }

    //delete the value of the row if exists at the given index
    public void removeValue(int index)
    {
        if(index >= row.size()) return;
        else{
            row.remove(index);
        }
    }

    //get the value back from the row given the index
    public Value getValue(Integer index) throws CommandExecutionException{
        if(index >= row.size()) throw new CommandExecutionException("fail to update the value of column "+
                table.getColumnByIndex(index).getName());
        return row.get(index);
    }

    //change the row to a string for printing
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Value value : row) {
            s.append(value.getContent()).append("\t");
        }
        return s.toString();
    }

}
