package dbStructure;

import command.common.Value;

public class Column {
    //the name of the column
    private String name;
    private Table table;
    //the ordinal position of this column in a row
    private int index;
    private Value.ValueType type;

    public Column(String name){
        this.name = name;
        type = null;
    }
    public Value.ValueType getType(){
        return type;
    }
    public void setType(Value.ValueType type){
        this.type = type;
    }
    public void setTable(Table table){
        this.table = table;
    }
    public void setIndex(int index){
        this.index = index;
    }
    public Table getTable(){
        return table;
    }
    public String getName(){
        return name;
    }
}
