package exception;

//exceptions happening during the tokenizing process such as "invalid token"
public class InvalidTokenException extends InvalidQueryException{
    public InvalidTokenException(String errorMessage){
        super(errorMessage);
    }
}
