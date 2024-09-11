package parse;

public class Token {
    private String content;
    public Boolean isSymbol;
    public Boolean isEndSymbol;
    public Boolean isAlphanumeric;
    public Boolean isEmpty;
    public boolean isStringLiteral;
    public Token(){
        content = null;
        isSymbol = false;
        isEndSymbol = false;
        isAlphanumeric = false;
        isEmpty = false;
        isStringLiteral=false;
    }
    public Token(String content){
        isSymbol = false;
        isEndSymbol = false;
        isAlphanumeric = false;
        isEmpty = false;
        isStringLiteral=false;
        this.content = content;
    }
    public void setIsStringLiteral(){
        isStringLiteral=true;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setIsEmpty(){
        isEmpty = true;
    }
    public void setIsSymbol(){
        isSymbol = true;
    }
    public String getContent(){
        return content;
    }
    public void setIsEndSymbol(){
        isEndSymbol = true;
    }
    public void setIsAlphanumeric(){
        isAlphanumeric=true;
    }
}