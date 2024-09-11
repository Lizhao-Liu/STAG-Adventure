package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistException
{
    int position;
    RowOrColumn type;

    public OutsideCellRangeException(int position, RowOrColumn type)
    {
        this.position = position;
        this.type = type;
    }

    public String toString() {
        return "The cell at " + type + (position+1) + " does not exist";
    }
}
