package command.Where;

import command.common.Value;
import dbStructure.Row;
import dbStructure.Table;
import exception.CommandExecutionException;

import java.util.ArrayList;

public class SingleExpr {
    String columnName;
    Value value;
    OperatorType operatorType;

    public Value getValue() {
        return value;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setOperator(OperatorType operator) {
        this.operatorType = operator;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public enum OperatorType{
        EQUAL, GREATER, LESS, EQUALORGREATER, EQUALORLESS, NOTEQUAL, LIKE
    }

    //get the rows which match the single expression
    public ArrayList<Integer> getRowIds(Table table) throws CommandExecutionException {
        int column = table.getColumnIndex(columnName);
        //check if the value type match the attribute type in a single expression
        if(table.getColumn(columnName).getType()!=value.getValueType()){
            throw new CommandExecutionException("attribute "+columnName +" cannot be converted to "+ value.getValueType());
        }
        //a record of the target row ids
        ArrayList<Integer> keys= new ArrayList<>();
        ArrayList<Row> rows = table.getRows();
        //walking through all the rows in the table and record the row ids which match the expression
        for(int i = 0; i<rows.size(); i++){
            if(isTarget(rows.get(i).getValue(column))){
                keys.add(rows.get(i).getId());
            }
        }
        return keys;
    }
    //check if there is a match
    boolean isTarget(Value element) {
        if (operatorType==OperatorType.EQUAL) return isEqual(element.getContent(),value.getContent());
        if (operatorType==OperatorType.EQUALORGREATER) return isGreaterOrEqual(element.getFloat(), value.getFloat());
        if (operatorType==OperatorType.LESS) return isLess(element.getFloat(), value.getFloat());
        if (operatorType==OperatorType.GREATER) return isGreater(element.getFloat(), value.getFloat());
        if (operatorType==OperatorType.EQUALORLESS) return isLessOrEqual(element.getFloat(), value.getFloat());
        if (operatorType==OperatorType.LIKE) return isLike(element.getContent(), value.getContent());
        if (operatorType==OperatorType.NOTEQUAL) return isNotEqual(element.getContent(), value.getContent());
        return false;
    }
    boolean isEqual(String element, String comparator){
        if(element.equals(comparator)) return true;
        return false;
    }
    boolean isGreaterOrEqual(Float element, Float comparator){
        if(element>=comparator) return true;
        return false;
    }
    boolean isLess(Float element, Float comparator){
        if(element<comparator) return true;
        return false;
    }
    boolean isGreater(Float element, Float comparator){
        if(element>comparator) return true;
        return false;
    }
    boolean isLessOrEqual(Float element, Float comparator){
        if(element<=comparator) return true;
        return false;
    }
    boolean isLike(String element, String comparator){
        if(comparator.length()>element.length()) return false;
        return element.contains(comparator);
    }
    boolean isNotEqual(String element, String comparator){
        if(element.equals(comparator)) return false;
        return true;
    }
}
