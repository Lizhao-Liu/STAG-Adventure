package parse;
import dbStructure.DatabaseManager;
import exception.DBException;
import exception.InvalidQueryException;
import command.*;
import command.Where.Condition;
import command.Where.SingleExpr;
import command.common.NameValuePair;
import command.common.Value;

import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private Tokenizer tokenizer;

    public Parser(String incomingStatement){ this.tokenizer = new Tokenizer(incomingStatement); }
    public CommandType start() throws InvalidQueryException 
    {
        try{
            return parseCommand();
        }catch (NullPointerException e){
            throw new InvalidQueryException("query terminates unexpectedly");
        }
    }
   public CommandType parseCommand() throws InvalidQueryException  {
        Token token = tokenizer.nextToken();
        if(token.isEmpty){throw new InvalidQueryException("invalid query: empty string");}
        switch(token.getContent().toUpperCase()){
            case "USE":
                return parseUse();
            case "CREATE":
                return parseCreate();
            case "DROP":
                return parseDrop();
            case "ALTER":
                return parseAlter();
            case "INSERT":
                return parseInsert();
            case "SELECT":
                return parseSelect();
            case "DELETE":
                return parseDelete();
            case "UPDATE":
                return parseUpdate();
            case "JOIN":
                return parseJoin();
            default:
                throw new InvalidQueryException("invalid Command");
        }
    }
    //BNF: USE <DatabaseName>
    CommandType parseUse() throws InvalidQueryException {
        Use use = new Use();
        use.setDbName(getAName(tokenizer.nextToken()));
        getTerminator(tokenizer.nextToken());
        return use;
    }

    //BNF: <CreateDatabase> | <CreateTable>
    CommandType parseCreate() throws InvalidQueryException  {
        Token token = tokenizer.nextToken();
        if (token.getContent().equalsIgnoreCase("DATABASE")) {
            return parseCreateDatabase();
        }
        else if(token.getContent().equalsIgnoreCase("TABLE")){
            return parseCreateTable();
        }
        else {
            throw new InvalidQueryException("invalid query");
        }
    }

    //BNF:CREATE DATABASE <DatabaseName>
    CommandType parseCreateDatabase() throws InvalidQueryException  {
        CreateDatabase createDB = new CreateDatabase();
        createDB.setDbName(getAName(tokenizer.nextToken()));
        getTerminator(tokenizer.nextToken());
        return createDB;
    }

    //BNF:CREATE TABLE <TableName> |
    //    CREATE TABLE <TableName> ( <AttributeList> )
    CommandType parseCreateTable() throws InvalidQueryException  {
        CreateTable createTable = new CreateTable();
        createTable.setTableName(getAName(tokenizer.nextToken()));
        if(tokenizer.peek().getContent().equals("(")){
            tokenizer.nextToken();
            createTable.setAttrList(getAttrList(")"));
        }
        getTerminator(tokenizer.nextToken());
        return createTable;
    }

    //BNF: DROP <Structure> <StructureName>
    CommandType parseDrop() throws InvalidQueryException  {
        Drop drop = new Drop();
        Token token = tokenizer.nextToken();
        if(token.getContent().equalsIgnoreCase("DATABASE")){
            drop.setType(Drop.StructureType.database);
        }
        else if(token.getContent().equalsIgnoreCase("TABLE")){
            drop.setType(Drop.StructureType.table);
        }
        else{
            throw new InvalidQueryException("invalid structure type");
        }
        drop.setName(getAName(tokenizer.nextToken()));
        getTerminator(tokenizer.nextToken());
        return drop;
    }

    //BNF: ALTER TABLE <TableName> <AlterationType> <AttributeName>
    CommandType parseAlter() throws InvalidQueryException  {
        Alter alter = new Alter();
        getKeyword("TABLE");
        alter.setTableName(getAName(tokenizer.nextToken()));

        //parse alteration type
        Token token = tokenizer.nextToken();
        if(token.getContent().equalsIgnoreCase("ADD")){
            alter.setType(Alter.AlterationType.ADD);
        }
        else if(token.getContent().equalsIgnoreCase("DROP")){
            alter.setType(Alter.AlterationType.DROP);
        }
        else{
            throw new InvalidQueryException("invalid query: missing alteration type");
        }

        alter.setColumnName(getAName(tokenizer.nextToken()));
        getTerminator(tokenizer.nextToken());
        return alter;
    }

    //BNF: INSERT INTO <TableName> VALUES ( <ValueList> )
    CommandType parseInsert() throws InvalidQueryException  {
        Insert insert = new Insert();
        getKeyword("INTO");
        insert.setTableName(getAName(tokenizer.nextToken()));
        getKeyword("VALUES");
        getKeyword("(");
        insert.setValueList(getValueList(")"));
        getTerminator(tokenizer.nextToken());
        return insert;
    }

    //SELECT <WildAttribList> FROM <TableName> |
    //SELECT <WildAttribList> FROM <TableName> WHERE <Condition>
    CommandType parseSelect() throws InvalidQueryException  {
        Select select = new Select();
        //parse WildAttribList
        if(tokenizer.peek().getContent().equals("*")){
            tokenizer.nextToken();
            getKeyword("FROM");
            select.setSelectAllCols();
        }
        else{
            select.setColumnList(getAttrList("FROM"));
        }
        select.setTableName(getAName(tokenizer.nextToken()));
        Token token = tokenizer.peek();
        if(token.isEndSymbol && tokenizer.peek(2).isEmpty){getTerminator(tokenizer.nextToken());}
        else if (token.getContent().equalsIgnoreCase("WHERE")){
            tokenizer.nextToken();
            select.setCondition(getCondition());
            getTerminator(tokenizer.nextToken());
        }
        else {throw new InvalidQueryException("invalid query: "+ token.getContent()+  " unexpected, add ; to terminate or use WHERE for conditions");}
        return select;
    }

    //DELETE FROM <TableName> WHERE <Condition>
    CommandType parseDelete() throws InvalidQueryException  {
        Delete delete = new Delete();
        getKeyword("FROM");
        delete.setTableName(getAName(tokenizer.nextToken()));
        getKeyword("WHERE");
        delete.setCondition(getCondition());
        getTerminator(tokenizer.nextToken());
        return delete;
    }

    //UPDATE <TableName> SET <NameValueList> WHERE <Condition>
    CommandType parseUpdate() throws InvalidQueryException  {
        Update update = new Update();
        update.setTableName(getAName(tokenizer.nextToken()));
        getKeyword("SET");
        update.setNameValuePairs(getNameValueList("WHERE"));
        update.setCondition(getCondition());
        getTerminator(tokenizer.nextToken());
        return update;
    }

    //BNF: JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>
    CommandType parseJoin() throws InvalidQueryException  {
        Join join = new Join();
        join.setTableName1(getAName(tokenizer.nextToken()));
        getKeyword("AND");
        join.setTableName2(getAName(tokenizer.nextToken()));
        getKeyword("ON");
        join.setColumnName1(getAName(tokenizer.nextToken()));
        getKeyword("AND");
        join.setColumnName2(getAName(tokenizer.nextToken()));
        getTerminator(tokenizer.nextToken());
        return join;
    }



    //          ****************************AUXILIARY FUNCTIONS BELOW*****************************

    //check if the next string is the keyword
    void getKeyword(String keyword) throws InvalidQueryException  {
        if(!tokenizer.nextToken().getContent().equalsIgnoreCase(keyword)) {
            throw new InvalidQueryException("invalid query: missing keyword: "+keyword);
        }
    }

    //get a column/table/database name which should be alphanumeric
    String getAName(Token token) throws InvalidQueryException
    {
        if(!token.isAlphanumeric){
            throw new InvalidQueryException("invalid column/table/database name");
        }
        return token.getContent();
    }

    //check if the next token is a semi colon
    void getTerminator(Token token) throws InvalidQueryException
    {
        if(token.isEndSymbol){
            return;
        }
        else{
            throw new InvalidQueryException("terminator missing");
        }
    }

    // parse the condition part of the query
    // BNF: ( <Condition> ) AND ( <Condition> )  |
    //      ( <Condition> ) OR ( <Condition> )   |
    //      <AttributeName> <Operator> <Value>
    Condition getCondition() throws InvalidQueryException  {
        ArrayList<Object> conditionList = getConditionList();
        return new Condition(conditionList);
    }

    //check the condition format and convert it to a list of object to prepare for converting the list to a binary search tree
    ArrayList<Object> getConditionList() throws InvalidQueryException  {
        Stack stack = new Stack();//using stack to check if the brackets match
        ArrayList<Object> recordList = new ArrayList<>();
        Token curr;
        int pos=0, separator=0;
        while(!(curr=tokenizer.peek()).isEndSymbol){
            tokenizer.nextToken();
            if(curr.isEmpty){
                throw new InvalidQueryException("invalid query: expected ;");
            }
            //push a "(" to stack if there is a left bracket and add it to the list
            if(curr.getContent().equals("(")){
                stack.push("(");
                recordList.add("(");
                pos++;
                continue;
            }

            //poop a ")" from the stack if there is a right bracket and add it to the list
            if(curr.getContent().equals(")")){
                if(stack.isEmpty()){throw new InvalidQueryException("invalid query: brackets not match");}
                recordList.add(")");
                pos++;
                stack.pop();
                continue;
            }

            // if there is an operator, add it to the list
            if(curr.getContent().equalsIgnoreCase("AND")||curr.getContent().equalsIgnoreCase("OR")){
                //making sure the brackets are formatted correctly
                //there should be only one operator out of all the brackets
                if(stack.isEmpty()){
                    if(separator!=0) throw new InvalidQueryException("missing brackets");
                    separator = pos;
                }
                recordList.add(curr.getContent());
                pos++;
            }
            //set up the single expression and add it to the list
            else if(curr.isAlphanumeric){
                recordList.add(getSingleExpr(curr));
                pos++;
            }
            else throw new InvalidQueryException("invalid query, unexpected "+ curr.getContent());
        }
        //the number of left brackets should be equal to the number of right bracketes
        if(!stack.isEmpty()) throw new InvalidQueryException("invalid query: brackets not match");
        return recordList;
    }

    //set up the single expression
    SingleExpr getSingleExpr(Token token) throws InvalidQueryException  {
        SingleExpr singleExpr = new SingleExpr();
        singleExpr.setColumnName(getAName(token));
        singleExpr.setOperator(getOperator(tokenizer.nextToken()));
        singleExpr.setValue(setUpValue(tokenizer.nextToken()));
        return singleExpr;
    }

    //get the operator and check if it is used with proper values following
    SingleExpr.OperatorType getOperator(Token token) throws InvalidQueryException  {
        if(token.getContent().equals("LIKE")){
            String next = tokenizer.peek().getContent();
            //the value should not be a integer or float after the operator "LIKE"
            if(isIntegerLiteral(next)||isFloatLiteral(next)){
                throw new InvalidQueryException("Invalid query: String expected after LIKE");
            }
            return SingleExpr.OperatorType.LIKE;
        }
        if(!token.isSymbol) throw new InvalidQueryException("invalid query: no operator found");
        String operator = token.getContent();
        while((token = tokenizer.peek()).isSymbol && !token.isEndSymbol){
            operator += token.getContent();
            tokenizer.nextToken();
        }
        SingleExpr.OperatorType type = getSymbolOperator(operator);
        //if the value is of boolean or string type, the operator should only be  <, >, <=, >=
        if(isBooleanLiteral(token.getContent()) || token.isStringLiteral){
            if(type!= SingleExpr.OperatorType.EQUAL && type!= SingleExpr.OperatorType.NOTEQUAL){
                throw new InvalidQueryException("expected numerical value after <, >, <=, >= but found "+ token.getContent());
            }
        }
        return type;
    }

    SingleExpr.OperatorType getSymbolOperator(String s) throws InvalidQueryException
    {
        if(s.equals("==")){return SingleExpr.OperatorType.EQUAL;}
        if(s.equals(">")) {return SingleExpr.OperatorType.GREATER;}
        if(s.equals("<")){return SingleExpr.OperatorType.LESS;}
        if(s.equals(">=")){return SingleExpr.OperatorType.EQUALORGREATER;}
        if(s.equals("<=")){return SingleExpr.OperatorType.EQUALORLESS;}
        if(s.equals("!=")){return SingleExpr.OperatorType.NOTEQUAL;}
        throw new InvalidQueryException("invalid query: expected an operator but found "+ s);
    }

    //get the list of attributes separated by comma before the right bracket
    ArrayList<String> getAttrList(String endOfList) throws InvalidQueryException  {
        ArrayList<String> attrList = new ArrayList<>();
        //curr starts at the first string of the attribute list
        Token curr;
        String attrName = "";
        //strings to be added to arraylist
        int toBeAdded = 0;
        while(!(curr=tokenizer.nextToken()).getContent().equalsIgnoreCase(endOfList)){
            if(curr.isEmpty|| curr.isEndSymbol){
                throw new InvalidQueryException("invalid query: missing "+ endOfList);
            }
            //if there is a comma, add the last attribute name to the list
            else if (curr.getContent().equals(",")) {
                //check the format is correct
                //every time it meets a comma, there should be one and only one attribute name waited to be added
                isValidInsertion(toBeAdded);
                attrList.add(attrName);
                toBeAdded--;
                continue;
            }
            //get the attribute name back and check if it is alphanumeric
            attrName = getAName(curr);
            toBeAdded++;
        }
        isValidInsertion(toBeAdded);
        attrList.add(attrName);
        return attrList;
    }

    ArrayList<Value> getValueList(String endOfList) throws InvalidQueryException  {
        ArrayList<Value> list = new ArrayList<>();
        Token curr;
        Value value = new Value();
        int toBeAdded = 0;
        while(!(curr=tokenizer.nextToken()).getContent().equalsIgnoreCase(endOfList)){
            if(curr.isEmpty|| curr.isEndSymbol){
                throw new InvalidQueryException("invalid query: missing" + endOfList);
            }
            else if (curr.getContent().equals(",")) {
                isValidInsertion(toBeAdded);
                list.add(value);
                toBeAdded--;
                continue;
            }
            value = setUpValue(curr);
            toBeAdded++;
        }
        isValidInsertion(toBeAdded);
        list.add(value);
        return list;
    }

    //get the list of attributes separated by comma before the ending symbol of the list
    ArrayList<NameValuePair> getNameValueList(String endOfList) throws InvalidQueryException  {
        ArrayList<NameValuePair> list = new ArrayList<>();
        Token curr;
        NameValuePair pair = new NameValuePair();
        int toBeAdded = 0;
        while(!(curr=tokenizer.nextToken()).getContent().equalsIgnoreCase(endOfList)){
            if(curr.isEmpty|| curr.isEndSymbol){
                throw new InvalidQueryException("invalid query: missing "+ endOfList);
            }
            if (curr.getContent().equals(",")) {
                isValidInsertion(toBeAdded);
                list.add(pair);
                toBeAdded--;
                continue;
            }
            //set up the next part to a name value pair
            pair=setUpNameValuePair(curr);
            toBeAdded++;
        }
        isValidInsertion(toBeAdded);
        list.add(pair);
        return list;
    }

    //set up name value pair <attribute name> = <value>
    NameValuePair setUpNameValuePair(Token curr) throws InvalidQueryException  {
        NameValuePair pair = new NameValuePair();
        pair.setColumnName(getAName(curr));
        if(!(curr = tokenizer.nextToken()).getContent().equals("=")) {
            throw new InvalidQueryException("invalid query: expected = but found "+ curr.getContent());
        }
        pair.setValue(setUpValue(tokenizer.nextToken()));
        return pair;
    }

    //set up value
    Value setUpValue(Token curr) throws InvalidQueryException
    {
        Value value = new Value();
        value.setContent(curr.getContent());
        if (curr.isStringLiteral) {
            value.setValueType(Value.ValueType.StringLiteral);
            return value;
        }
        else if(isIntegerLiteral(curr.getContent())){
            value.setValueType(Value.ValueType.IntegerLiteral);
            return value;
        }
        else if(isFloatLiteral(curr.getContent())){
            value.setValueType(Value.ValueType.FloatLiteral);
            return value;
        }
        else if(isBooleanLiteral(curr.getContent())){
            value.setValueType(Value.ValueType.BooleanLiteral);
            return value;
        }
        throw new InvalidQueryException("invalid query: expected a value e.g. '<StringLiteral>' ," +
                " <BooleanLiteral> , <FloatLiteral> , <IntegerLiteral>");
    }

    //during the parsing process in a list, every time it meets a comma, there should be one and only one attribute name waited to be added
    void isValidInsertion(int toBeAdded) throws InvalidQueryException
    {
        if(toBeAdded == 1) return;
        if(toBeAdded == 0){
            throw new InvalidQueryException("missing element between comma");
        }
        throw new InvalidQueryException("Missing comma");
    }

    boolean isBooleanLiteral(String target){
        return target.equals("true") || target.equals("false");
    }

    boolean isFloatLiteral(String target){
        try{
            Float.parseFloat(target);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }

    boolean isIntegerLiteral (String target){
        try{
            Integer.parseInt(target);
            return true;
        }catch(NumberFormatException nfe){
            return false;
        }
    }

    public static void main(String[] args){
        DatabaseManager manager = new DatabaseManager();
        String[] incomingCommands = {"Drop DATABASE markbook;", "CREATE DATABASE markbook;", "USE markbook;", "CREATE TABLE marks (name, mark, pass);", "INSERT INTO marks VALUES ('ll', 22, true);" };
        for(int i =0; i < incomingCommands.length; i++){
            Parser parser = new Parser(incomingCommands[i]);
            try {
                CommandType cmd = parser.start();
                cmd.execute(manager);
                System.out.println("OK");
            } catch (DBException e) {
                System.out.println(e);
            }
        }

    }
}