package exception;

//exceptions happening during the parsing process such as "missing comma"
public class InvalidQueryException extends DBException{

    public InvalidQueryException(String errorMessage) {
        super(errorMessage);
    }
}
