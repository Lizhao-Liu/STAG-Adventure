package parse;

import exception.InvalidTokenException;

public class Tokenizer {
    private int pos;
    private int length;
    private char[] currLine;

    public Tokenizer(String line){
        pos = 0;
        this.currLine=line.toCharArray();
        length = line.length();
    }

    //peek the next token but not change the value of pos(position in line)
    public Token peek() throws InvalidTokenException {
        int origPos = pos;
        Token next = nextToken();
        pos = origPos;
        return next;
    }
    public Token peek(int n) throws InvalidTokenException {
        int origPos = pos;
        Token next = new Token();
        while(n>0){
            next = nextToken();
            n--;
        }
        pos = origPos;
        return next;
    }

    public Token nextToken() throws InvalidTokenException {
        Token token = new Token();
        //skip the blanks
        while (pos<length && isBlank(currLine[pos])) {
            pos++;
        }
        //set up an empty token
        if(pos>=length){
            token.setIsEmpty();
            return token;
        }
        //find the next token type and get the returned token
        char curr = currLine[pos];
        if (isSymbol(curr)) {
            return nextSymbol(curr);
        }
        if (isSingleQuote(curr)) {
            return nextQuoteLine();
        }
        if(isDigital(curr)){
            //check if the next token is a float with a decimal point in it
            if((token = nextFloat())!=null) return token;
        }
        if (isLetter(curr) || isDigital(curr)) {
            return nextString();
        }
        throw new InvalidTokenException("invalid token" + curr);
    }

    ////set up the token content as the symbol and the type as symbol
    public Token nextSymbol(char curr)
    {
        Token token = new Token();
        token.setContent(Character.toString(curr));
        token.setIsSymbol();
        if(curr==';'){
            token.setIsEndSymbol();
        }
        pos++;
        return token;
    }

    //set up the token type as float and save the content if the next string is a float number string
    public Token nextFloat()
    {
        int count= 0;
        int i = pos;
        StringBuilder s = new StringBuilder();
        Token token = null;
        while(i<length && (isDigital(currLine[i])||currLine[i]=='.')){
            if(currLine[i]=='.'){count++;}
            s.append(currLine[i]);
            i++;
        }
        if((i-pos)>2 && count==1 && (currLine[i-1]!='.')){
            token = new Token(s.toString());
            pos = i;
        }
        return token;
    }

    //set up the next token content as the next string and the type as alphanumeric
    public Token nextString(){
        StringBuilder s = new StringBuilder();
        while(pos<length&&(isLetter(currLine[pos])||isDigital(currLine[pos]))){
            s.append(currLine[pos]);
            pos++;
        }
        Token token = new Token(s.toString());
        token.setIsAlphanumeric();
        return token;
    }

    //set up the token content as the string inside the single quotes and set up the token type as StringLiteral
    public Token nextQuoteLine() throws InvalidTokenException{
        StringBuilder s = new StringBuilder();
        pos++;
        try{
            while(!isSingleQuote(currLine[pos])){
                s.append(currLine[pos]);
                pos++;
            }
            pos++;
            Token token = new Token(s.toString());
            token.setIsStringLiteral();
            return token;
        }catch(ArrayIndexOutOfBoundsException e){
            throw new InvalidTokenException("Missing close quote");
        }

    }

    private boolean isSymbol(char c){
        return (c == '>' || c == '<' || c == '=' || c == '*' ||
                c == ',' || c == '(' || c == ')'||c==';'|| c=='!');
    }

    private boolean isLetter(char c){
        return (c>='a'&&c<='z')||(c>='A'&&c<='Z');
    }

    private boolean isDigital(char c){
        return((c>='0'&&c<='9'));
    }

    private boolean isBlank(char c){
        return (c==' '||c=='\n'||c=='\t');
    }

    private boolean isSingleQuote(char c){
        return c=='\'';
    }
}
