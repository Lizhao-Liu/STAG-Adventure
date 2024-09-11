package OXOExceptions;

public class CellAlreadyTakenException extends OXOMoveException
{
    public CellAlreadyTakenException(int row, int column)
    {
        super(row, column);
    }
    public String toString() {
        return "The cell at row: " + (super.getRow()+1) + " column: " + (super.getColumn()+1) + " is already taken";
    }
}

