package command.common;

//a class to storing a name value pair structure e.g. <AttributeName> = <Value>
public class NameValuePair{
    String columnName;
    Value value;

    public void setColumnName(String name){
        this.columnName = name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }
}