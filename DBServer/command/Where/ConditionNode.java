package command.Where;

import dbStructure.Table;
import exception.CommandExecutionException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConditionNode {
    private ConditionNodeType type;
    private SingleExpr expression;
    private ConditionNode left;
    private ConditionNode right;

    //initialize an operator node
    public ConditionNode(String nodeTypeText){
        if(nodeTypeText.equals("OR")){
            this.type=ConditionNodeType.OR;
        }else if(nodeTypeText.equals("AND")){
            this.type=ConditionNodeType.AND;}
        else{
            this.type=ConditionNodeType.UNDEFINED;
        }
        expression = null; left = null; right = null;
    }

    //initialize a singleExpr node which is also the leaf node in this case
    public ConditionNode(SingleExpr expr){
        this.expression = expr;
        this.type = ConditionNodeType.EXPRESSION;
        left = null; right = null;
    }

    public void setType(ConditionNodeType type){
        this.type = type;
    }
    public ConditionNodeType getType(){
        return type;
    }
    public void setLeft(ConditionNode leftNode) {
        this.left = leftNode;
    }
    public void setRight(ConditionNode rightNode) {
        this.right = rightNode;
    }


    public ArrayList<Integer> getRowList(Table table) throws CommandExecutionException {
        //if the operator is AND, find the common rows from the two returned lists (one from left and one from right)
        if(this.type==ConditionNodeType.AND){
            ArrayList<Integer> res = new ArrayList<Integer>(left.getRowList(table));
            res.retainAll(right.getRowList(table));
            return res;
        }
        //if the operator is OR, return back all the rows appearing in the two returned lists
        if(this.type==ConditionNodeType.OR){
            Set<Integer> set = new HashSet<>(left.getRowList(table));
            set.addAll(right.getRowList(table));
            ArrayList<Integer> res = new ArrayList<>(set);
            return res;
        }
        else{
            return expression.getRowIds(table);
        }
    }
    public enum ConditionNodeType{OR, AND, EXPRESSION, UNDEFINED}
}

