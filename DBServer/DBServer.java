import command.CommandType;
import dbStructure.DatabaseManager;
import exception.DBException;
import parse.Parser;

import java.io.*;
import java.net.*;

class DBServer
{
    DatabaseManager manager;
    public DBServer(int portNumber)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            manager= new DatabaseManager();
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException {
        String incomingCommand = socketReader.readLine();
        Parser parser = new Parser(incomingCommand);
        try {
            CommandType cmd = parser.start();
            cmd.execute(manager);
            socketWriter.write("[OK]");
            socketWriter.write("\n" + ((char) 4) + "\n");
            socketWriter.flush();
            socketWriter.write(cmd.getOutput());
            socketWriter.write("\n" + ((char) 4) + "\n");
            socketWriter.flush();
        } catch (DBException e) {
            socketWriter.write(e.toString());
            socketWriter.write("\n" + ((char) 4) + "\n");
            socketWriter.flush();
        }


    }
    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }


}
