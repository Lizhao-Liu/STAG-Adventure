import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;

    public OXOController(OXOModel model)
    {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        if(gameModel.getWinner()==null){
            checkValidCommand(command);
            int col = command.charAt(1) - '1';
            int row = command.toLowerCase().charAt(0) - 'a';
            gameModel.setCellOwner(row, col, gameModel.getCurrentPlayer());
            if(WinDetected(row, col)){
                gameModel.setWinner(gameModel.getCurrentPlayer());
            }
            gameModel.addAMovement();
            if(gameModel.getMovements()== gameModel.getNumberOfColumns()*gameModel.getNumberOfRows()){
                gameModel.setGameDrawn();
            }
            gameModel.setCurrentPlayer(gameModel.getCurrentPlayer());
        }
    }

    void checkValidCommand(String command) throws OXOMoveException
    {
        if(command.length()!=2){
            throw new InvalidIdentifierLengthException(command.length());
        }
        int col = command.charAt(1) - '1';
        int row = command.toLowerCase().charAt(0) - 'a';
        if(row<0||row>9){
            throw new InvalidIdentifierCharacterException(command.charAt(0), RowOrColumn.ROW);
        }
        if(col<0||col>9){
            throw new InvalidIdentifierCharacterException(command.charAt(1), RowOrColumn.COLUMN);
        }
        if(row>=gameModel.getNumberOfRows()){
            throw new OutsideCellRangeException(row, RowOrColumn.ROW);
        }
        if(col>=gameModel.getNumberOfColumns()){
            throw new OutsideCellRangeException(col, RowOrColumn.COLUMN);
        }
        if(gameModel.getCellOwner(row, col).getPlayingLetter()!='\0'){
            throw new CellAlreadyTakenException(row, col);
        }
    }

    boolean WinDetected(int row, int col){
        char curr = gameModel.getCellOwner(row, col).getPlayingLetter();
        int count = 0;
        int i ,j;

        // check for wins in vertical direction
        i = row;
        while(i>=0 && gameModel.getCellOwner(i, col).getPlayingLetter()==curr){
            i--;
            count++;
        }
        i = row+1;
        while(i<gameModel.getNumberOfRows() && gameModel.getCellOwner(i, col).getPlayingLetter()==curr){
            i++;
            count++;
        }
        if(count == gameModel.getWinThreshold()){
            return true;
        }

        // check for wins in horizontal direction
        count = 0;
        i=col;
        while(i>=0 && gameModel.getCellOwner(row, i).getPlayingLetter()==curr){
            i--;
            count++;
        }
        i = col+1;
        while(i<gameModel.getNumberOfColumns() && gameModel.getCellOwner(row, i).getPlayingLetter()==curr){
            i++;
            count++;
        }
        if(count == gameModel.getWinThreshold()){
            return true;
        }

        //check win condition diagonally
        count = 0;
        i=row; j=col;
        while(i>=0&&j>=0&&gameModel.getCellOwner(i, j).getPlayingLetter()==curr){
            i--;
            j--;
            count++;
        }
        i=row+1; j=col+1;
        while(i<gameModel.getNumberOfRows()&&j<gameModel.getNumberOfColumns()&&gameModel.getCellOwner(i, j).getPlayingLetter()==curr){
            i++;
            j++;
            count++;
        }
        if(count == gameModel.getWinThreshold()){
            return true;
        }

        //check win condition anti-diagonally
        count = 0;
        i=row; j=col;
        while(i>=0&&j<gameModel.getNumberOfColumns()&&gameModel.getCellOwner(i, j).getPlayingLetter()==curr){
            i--;
            j++;
            count++;
        }
        i = row+1; j=col-1;
        while(i<gameModel.getNumberOfRows()&&j>=0&&gameModel.getCellOwner(i, j).getPlayingLetter()==curr){
            i++;
            j--;
            count++;
        }
        if(count == gameModel.getWinThreshold()){
            return true;
        }
        return false;
    }
}
