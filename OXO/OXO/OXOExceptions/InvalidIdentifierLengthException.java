package OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException
{
    int length;
    public InvalidIdentifierLengthException(int length)
    {
        this.length = length;
    }

    public String toString() {
        return "Invalid Identifier length: expected 2 characters, get " +length +" characters";
    }
}
