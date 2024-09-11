package exception;

public abstract class DBException extends Exception
{
    String errorMessage;
    public DBException(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String toString(){
        return "[ERROR] " + errorMessage;
    }
}
