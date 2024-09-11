package exception;

//exceptions happening during the execution process such as "table doesn't exist"
public class CommandExecutionException extends DBException{
    public CommandExecutionException(String errorMessage) {
        super(errorMessage);
    }
}
