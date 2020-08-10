package sample;

import java.io.*;
import java.net.*;

public class Server {


    public static int[][] boardPlayer1;
    public static int[][] boardPlayer2;

   public ObjectOutputStream outputStreamPlayer1;
   public  ObjectInputStream inputStreamPlayer1;

    public ObjectOutputStream outputStreamPlayer2;
    public ObjectInputStream inputStreamPlayer2;

    public Server() throws SocketException  {

    }

    public void receivePlayerOneBoard() throws IOException, ClassNotFoundException {


        ServerSocket serverSocket = new ServerSocket(9876);
        Socket socket = serverSocket.accept();

         outputStreamPlayer1 = new ObjectOutputStream(socket.getOutputStream());
         inputStreamPlayer1 = new ObjectInputStream(socket.getInputStream());

        int[][] arrFromClient = (int[][])inputStreamPlayer1.readObject();
        System.out.println("Serwer odebral tablice");
        for (int[] ints : arrFromClient) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println(" ");
        }

        boardPlayer1 = arrFromClient;
        serverSocket.close();

    }

    public void receivePlayerTwoBoard() throws IOException, ClassNotFoundException {


        ServerSocket serverSocket = new ServerSocket(9877);
        Socket socket = serverSocket.accept();

        outputStreamPlayer2 = new ObjectOutputStream(socket.getOutputStream());
        inputStreamPlayer2 = new ObjectInputStream(socket.getInputStream());

        int[][] arrFromClient = (int[][])inputStreamPlayer2.readObject();
        System.out.println("Serwer odebral tablice");
        for (int[] ints : arrFromClient) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println(" ");
        }
        serverSocket.close();
        boardPlayer2 = arrFromClient;
    }


    public static void main(String args[]) throws Exception
    {
        Server server = new Server();
        server.receivePlayerOneBoard();
        BattleShip b1 = new BattleShip(9876);

        boolean firstok = false;
        boolean secondtok = false;

        //server.receivePlayerTwoBoard();
        //BattleShip b2 = new BattleShip(9877);


        if(boardPlayer2 != null){
            server.outputStreamPlayer1.writeObject(boardPlayer2);
            b1.playerBoardInt = boardPlayer2;
            firstok=true;

        }
        if(boardPlayer1 != null){
            server.outputStreamPlayer2.writeObject(boardPlayer1);
           // b2.playerBoardInt = boardPlayer1;
            secondtok=true;
        }
        if(firstok && secondtok){
            //b2.running=true;
            b1.running=true;
        }

    }

}
