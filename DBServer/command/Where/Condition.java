package command.Where;

import dbStructure.Table;
import exception.CommandExecutionException;
import exception.InvalidQueryException;

import java.util.ArrayList;

//deal with conditions in query using binary search tree structure
public class Condition {
    ConditionNode root;

    public Condition(ArrayList<Object> list) throws InvalidQueryException {
        root= buildConditionTree(list, 0, list.size());
    }

    //build the tree with the operators as the separator nodes and the SingleExpr as the leaf nodes
    ConditionNode buildConditionTree(ArrayList<Object> list, int start, int end) throws InvalidQueryException {
        int separator;
        //start==end-1 indicates that it is the leaf node of the tree and there should be a singleExpr object
        if(start == end - 1){
            Object leafExpr = list.get(start);
            if(leafExpr instanceof SingleExpr) {
                ConditionNode leafNode = new ConditionNode((SingleExpr) leafExpr);
                return leafNode;
            }
            else throw new InvalidQueryException("invalid condition format, please check the brackets");
        }
        //find the operator at the top layer (with the use of brackets)
        // e.g. ((SingleExpr1) and (SingleExpr2)) or (SingleExpr3), "or" should be the separator node
        separator = findSeparator(list, start, end);
        //if the separator not found, removing the redundant brackets at the outer layer
        if(separator==-1) return buildConditionTree(list, start+1, end-1);
        ConditionNode node = new ConditionNode((String)list.get(separator));
        //recursively set the tree nodes until it reaches the leafnode level
        node.setLeft(buildConditionTree(list, start, separator));
        node.setRight(buildConditionTree(list, separator+1, end));
        return node;
    }

    public int findSeparator(ArrayList<Object> list, int start, int end) throws InvalidQueryException {
        int depth = 0;
        for(int i = start; i<end; i++){
            if(list.get(i) instanceof String){
                String temp= (String) list.get(i);
                //if there is a left bracket, depth increases by one
                if(temp.equals("(")){
                    depth++;
                    //if there is a right bracket, depth decreases by one
                }else if(temp.equals(")")){
                    depth--;
                    //if it is an operator and the depth equals to one, the separator is found
                }else if(temp.equalsIgnoreCase("OR") || temp.equalsIgnoreCase("AND")){
                    if(list.get(i-1)!=")" || list.get(i+1)!="(") throw new InvalidQueryException("invalid query: wrong condition format");
                    if(depth==0){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    //get back the row ids which match the condition
    public ArrayList<Integer> getRowList(Table table) throws CommandExecutionException {
        return root.getRowList(table);
    }

}

