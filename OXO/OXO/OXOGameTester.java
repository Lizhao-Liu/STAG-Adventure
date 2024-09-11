import OXOExceptions.OXOMoveException;

public class OXOGameTester {
    public static void main(String[] args){
        boolean assertionsEnabled = false;
        assert(assertionsEnabled = true);
        if (assertionsEnabled) {
            System.out.println("Test Exception started..");
            testException();
            System.out.println("Test Exception passed.\n\n");
            System.out.println("Test WinDetection started..");
            testWinDetection();
            System.out.println("Test WinDetection passed.\n\n");
            System.out.println("Test GameDrawn started..");
            testGameDrawn();
            System.out.println("Test GameDrawn passed.\n\n");
            System.out.println("Test Player Management started..");
            testManagePlayers();
            System.out.println("Test Player Management passed.\n\n");
            System.out.println("Test Board of Different Sizes started..");
            testDiffSizeBoard();
            System.out.println("Test Board of Different Sizes passed.\n\n");
            System.out.println("Test Different Win Threshold started..");
            testDiffWinThresh();
            System.out.println("Test Different Win Threshold passed.\n\n");
            System.out.println("SUCCESS: All tests passed !!!");
        }
        else {
            System.out.println("You MUST run java with assertions enabled (-ea) to test your program !");
        }
    }
    private static void testException()
    {
        OXOModel model = new OXOModel(3,3,3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        OXOController controller = new OXOController(model);
        System.out.println("\ntrying to add a command of invalid identifier length...");
        try{
            controller.handleIncomingCommand("a");
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        System.out.println("\ntrying to add a command of invalid identifier length...");
        try{
            controller.handleIncomingCommand("mmm");
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        System.out.println("\ntrying to add a command with invalid row identifier character...");
        try{
            controller.handleIncomingCommand("33");
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        System.out.println("\ntrying to add a command with invalid column identifier character...");
        try{
            controller.handleIncomingCommand("al");
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        System.out.println("\ntrying to add a command with the row outside the cell range...");
        try{
            controller.handleIncomingCommand("f1");
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        System.out.println("\ntrying to add a command with the column outside the cell range...");
        try{
            controller.handleIncomingCommand("a5");
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        System.out.println("\ntrying to take a cell which is already taken...");
        try{
            controller.handleIncomingCommand("c1");
            controller.handleIncomingCommand("c1");
        }catch(OXOMoveException e){
            System.out.println(e);
        }
    }
    private static void testWinDetection()
    {
        // check for wins in horizontal direction
        OXOModel model1 = new OXOModel(3,3,3);
        model1.addPlayer(new OXOPlayer('X'));
        model1.addPlayer(new OXOPlayer('O'));
        OXOController controller1 = new OXOController(model1);
        try{
            controller1.handleIncomingCommand("a1");
            controller1.handleIncomingCommand("b2");
            controller1.handleIncomingCommand("a2");
            controller1.handleIncomingCommand("b1");
            assert(model1.getWinner()==null);
            controller1.handleIncomingCommand("a3");
            assert(model1.getWinner()!=null);
            assert(model1.getWinner().getPlayingLetter()=='X');
            controller1.handleIncomingCommand("b3");
            assert(model1.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        // check for wins in vertical direction
        OXOModel model2 = new OXOModel(3,3,3);
        model2.addPlayer(new OXOPlayer('X'));
        model2.addPlayer(new OXOPlayer('O'));
        OXOController controller2 = new OXOController(model2);
        try{
            controller2.handleIncomingCommand("a1");
            controller2.handleIncomingCommand("a2");
            controller2.handleIncomingCommand("b1");
            controller2.handleIncomingCommand("c2");
            assert(model2.getWinner()==null);
            controller2.handleIncomingCommand("c1");
            assert(model2.getWinner()!=null);
            assert(model2.getWinner().getPlayingLetter()=='X');
            controller2.handleIncomingCommand("b2");
            assert(model2.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        // check for wins in diagonal direction
        OXOModel model3 = new OXOModel(3,3,3);
        model3.addPlayer(new OXOPlayer('X'));
        model3.addPlayer(new OXOPlayer('O'));
        OXOController controller3 = new OXOController(model3);
        try{
            controller3.handleIncomingCommand("a1");
            controller3.handleIncomingCommand("a2");
            controller3.handleIncomingCommand("b2");
            controller3.handleIncomingCommand("c1");
            assert(model3.getWinner()==null);
            controller3.handleIncomingCommand("c3");
            assert(model3.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        // check for wins in anti-diagonal direction
        OXOModel model4 = new OXOModel(3,3,3);
        model4.addPlayer(new OXOPlayer('X'));
        model4.addPlayer(new OXOPlayer('O'));
        OXOController controller4 = new OXOController(model4);
        try{
            controller4.handleIncomingCommand("a3");
            controller4.handleIncomingCommand("a2");
            controller4.handleIncomingCommand("b2");
            controller4.handleIncomingCommand("c2");
            assert(model4.getWinner()==null);
            controller4.handleIncomingCommand("c1");
            assert(model4.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

    }
    private static void testGameDrawn()
    {
        OXOModel model1 = new OXOModel(3,3,3);
        model1.addPlayer(new OXOPlayer('X'));
        model1.addPlayer(new OXOPlayer('O'));
        OXOController controller1 = new OXOController(model1);
        try{
            controller1.handleIncomingCommand("a1");
            controller1.handleIncomingCommand("a2");
            controller1.handleIncomingCommand("b1");
            controller1.handleIncomingCommand("c1");
            controller1.handleIncomingCommand("b2");
            controller1.handleIncomingCommand("c3");
            controller1.handleIncomingCommand("c2");
            controller1.handleIncomingCommand("b3");
            controller1.handleIncomingCommand("a3");
            assert(model1.getWinner()==null);
            assert(model1.isGameDrawn());
        }catch(OXOMoveException e){
            System.out.println(e);
        }
    }
    private static void testManagePlayers()
    {
        OXOModel model = new OXOModel(3,3,3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        OXOController controller = new OXOController(model);
        assert(model.getCurrentPlayer().getPlayingLetter()=='X');
        try{
            controller.handleIncomingCommand("a1");
            assert(model.getCellOwner(0,0).getPlayingLetter()=='X');
            assert(model.getCurrentPlayer().getPlayingLetter()=='O');
            controller.handleIncomingCommand("a2");
            assert(model.getCellOwner(0,1).getPlayingLetter()=='O');
            assert(model.getCurrentPlayer().getPlayingLetter()=='X');
            controller.handleIncomingCommand("b2");
            System.out.println("Trying to add a duplicated player X...");
            model.addPlayer(new OXOPlayer('X'));
            model.addPlayer(new OXOPlayer('Z'));
            assert(model.getNumberOfPlayers()==3);
            assert(model.getPlayerByNumber(2).getPlayingLetter()=='Z');
            controller.handleIncomingCommand("b3");
            assert(model.getCellOwner(1,2).getPlayingLetter()=='Z');
        }catch(OXOMoveException e){
            System.out.println(e);
        }
    }
    private static void testDiffSizeBoard()
    {
        OXOModel model1 = new OXOModel(5,5,3);
        model1.addPlayer(new OXOPlayer('X'));
        model1.addPlayer(new OXOPlayer('O'));
        OXOController controller1 = new OXOController(model1);
        try{
            controller1.handleIncomingCommand("a5");
            controller1.handleIncomingCommand("b5");
            controller1.handleIncomingCommand("b4");
            controller1.handleIncomingCommand("a3");
            controller1.handleIncomingCommand("c3");
            assert(model1.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        OXOModel model2 = new OXOModel(7,4,3);
        model2.addPlayer(new OXOPlayer('X'));
        model2.addPlayer(new OXOPlayer('O'));
        OXOController controller2 = new OXOController(model2);
        try{
            controller2.handleIncomingCommand("a3");
            controller2.handleIncomingCommand("c1");
            controller2.handleIncomingCommand("f3");
            controller2.handleIncomingCommand("d2");
            controller2.handleIncomingCommand("f4");
            controller2.handleIncomingCommand("e3");
            assert(model2.getWinner().getPlayingLetter()=='O');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        OXOModel model3 = new OXOModel(2,2,2);
        model3.addPlayer(new OXOPlayer('X'));
        model3.addPlayer(new OXOPlayer('O'));
        OXOController controller = new OXOController(model3);
        try{
            controller.handleIncomingCommand("a1");
            controller.handleIncomingCommand("a2");
            controller.handleIncomingCommand("b2");
            assert(model3.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }
    }
    private static void testDiffWinThresh()
    {
        OXOModel model1 = new OXOModel(3,3,2);
        model1.addPlayer(new OXOPlayer('X'));
        model1.addPlayer(new OXOPlayer('O'));
        OXOController controller1 = new OXOController(model1);
        try{
            controller1.handleIncomingCommand("a1");
            controller1.handleIncomingCommand("a2");
            controller1.handleIncomingCommand("a3");
            assert(model1.getWinner()==null);
            controller1.handleIncomingCommand("b2");
            assert(model1.getWinner().getPlayingLetter()=='O');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        OXOModel model2 = new OXOModel(3,3,2);
        model2.addPlayer(new OXOPlayer('X'));
        model2.addPlayer(new OXOPlayer('O'));
        OXOController controller2 = new OXOController(model2);
        try{
            controller2.handleIncomingCommand("a1");
            controller2.handleIncomingCommand("a2");
            controller2.handleIncomingCommand("a3");
            assert(model2.getWinner()==null);
            controller2.handleIncomingCommand("b1");
            assert(model2.getWinner().getPlayingLetter()=='O');
        }catch(OXOMoveException e){
            System.out.println(e);
        }

        OXOModel model3 = new OXOModel(3,3,1);
        model3.addPlayer(new OXOPlayer('X'));
        model3.addPlayer(new OXOPlayer('O'));
        OXOController controller = new OXOController(model3);
        try{
            controller.handleIncomingCommand("a1");
            assert(model3.getWinner().getPlayingLetter()=='X');
        }catch(OXOMoveException e){
            System.out.println(e);
        }
    }
}
