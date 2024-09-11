package OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException{
    char character;
    RowOrColumn type;
    public InvalidIdentifierCharacterException(char character, RowOrColumn type){
        this.character = character;
        this.type = type;
    }
    public String toString() {
        return "The " + type +" character "+ character + " is not valid";
    }
}
