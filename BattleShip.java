package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.tree.ExpandVetoException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class BattleShip extends Application {

    GameBoard playerBoard;
    GameBoard enemyBoard;
    public boolean running = false;
    private boolean pickTime = false;
    private boolean myTurn = false;
    private boolean player1 = false;
    private boolean endGame = false;
    public int size;
    public int ships;
    private int shipsToHit;
    public BattleShipClient client;
    private int port;
    private String serverAdress;
    private String playerName;
    public int[][] playerBoardInt;
    public boolean Ready=false;
    private static Vector<int[][]> PlayerBoards = new Vector<>();
    private boolean received = false;
    private String Boards;
    private boolean rec = false;


    public BattleShip(int p) {
        port = p;
    }



    public Parent createContent() throws Exception {



        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        if(ships == 2) {
            ships = 9;
            shipsToHit = ships;
            root.setRight(new Text("Masz do wyboru 2 statki" + '\n'
                                    + "Pierwszy - 5 polowy" + '\n'
                                    + "Drugi - 4 polowy"));
        }
        else if(ships == 3){
            ships = 12;
            shipsToHit = ships;
            root.setRight(new Text("Masz do wyboru 3 statki" + '\n'
                    + "Pierwszy - 5 polowy" + '\n'
                    + "Drugi - 4 polowy" + '\n'
                    + "Trzeci - 3 polowy"));
        }
        else if(ships == 5){
            ships = 14;
            shipsToHit = ships;
            root.setRight(new Text("Masz do wyboru 3 statki" + '\n'
                    + "Pierwszy - 5 polowy" + '\n'
                    + "Drugi - 4 polowy" + '\n'
                    + "Trzeci - 3 polowy" + '\n'
                    + "Czwarty - 2 polowy" + '\n'
                    + "Piąty - 1 polowy"));
        }

        enemyBoard = new GameBoard(true, event -> {
            Thread t = new Thread(() -> {



            if(!endGame && pickTime){

                Cell cell = (Cell) event.getSource();
                int counter1=0;
                int counter2=0;
                String ans = "";


                System.out.println(cell.x + " " + cell.y);
                System.out.println(endGame + "" + pickTime + "" + myTurn);

                if(BattleShipClient.counter1<10) {
                    BattleShipClient.counter = 0;

                        if (enemyBoard.placeShip(new Ship(event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {

                            beginMarking(cell.x, cell.y);
                            //System.out.println("Wysylam picka : " + String.valueOf(cell.x) + String.valueOf(cell.y) );
                            client.SendRequest("tab" + String.valueOf(cell.x) + String.valueOf(cell.y));
                            BattleShipClient.counter++;
                            System.out.println("Licznik: " + BattleShipClient.counter++);
                            rec = false;
                        }


                    while (!rec) {
                        try {
                            System.out.println("Probuje odebrac");
                            ans = client.receiveRequest();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (ans.endsWith("Not")) {
                            System.out.println("jescze nie gotowe do odebrania");
                        }
                        if (ans.endsWith(" ")) {
                            System.out.println("Jest gotowe");
                            rec = true;
                            System.out.println("Odberalem: " + ans);
                            BattleShipClient.counter1++;
                        }
                    }
                }

                   /*
               if(player1 && !myTurn){
                    String answer = "";
                    try {
                        answer = client.receiveRequest();
                        System.out.println("Pick otrzymany z serwera: " +answer);
                        myTurn=true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(!player1 && !myTurn){
                    String answer = "";
                    try {
                        answer = client.receiveRequest();
                        System.out.println("Pick otrzymany z serwera: " +answer);
                        myTurn=true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

              */
            }
            });
            t.start();


        },size);

        playerBoard = new GameBoard(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if(ships > 0){
                if (playerBoard.placeShip(new Ship(event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                    playerBoard.board[cell.y][cell.x]=1;
                    ships--;
                }
            }
            else if(ships == 0) {
                running = true;
                try {
                    client = new BattleShipClient(port,serverAdress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // wysyłam wypełnioną tablicę do serwera
                try {
                    BattleShipClient.SendBoardToServer(playerBoard.board);
                    System.out.println("Wysylam tablice od gracza na serwer" + port);
                    //root.setRight(new Text("Wysłałem tablice do gracza 2" + '\n'
                           // + "Czekaj teraz na ruch drugiego gracza" ));


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Thread t = new Thread(() -> {
                    String req="";
                    while (!received){
                        try {
                            client.SendRequest("Ready?");
                            req = client.receiveRequest();
                           // System.out.println("Probuje odebrac");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(req.endsWith("No")){
                            //System.out.println("Nie gotowe");
                        } else if(req.endsWith(" ")){
                            System.out.println("Jest ready");
                            Boards = req;
                            beginGame();

                            try {
                                received=true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                t.start();
            }

        },size);


        VBox vbox = new VBox(50, playerBoard,enemyBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    // funkcja wpisuje w tablice graczy zaznaczone wartosci przeslane
    // przez serwer

    public void beginGame() {

        char[] tab = Boards.toCharArray();
        System.out.println(Boards);
        System.out.println( "rozmiar tab = " + tab.length);

        if(tab.length==54){
            for(int i=tab.length/2; i<tab.length-2; i+=3){
                if(tab[i]!=' '){
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i+1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i+1]))][Integer.parseInt(String.valueOf(tab[i]))]=1;
                }
            }
            myTurn=true;
            player1=true;
        }
        else if(tab.length==58){
            for(int i = 4; i<tab.length/2; i+=3){
                if(tab[i]!=' '){
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i+1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i+1]))][Integer.parseInt(String.valueOf(tab[i]))]=1;
                }
            }
            myTurn=true;
            player1=false;
        }


        System.out.println("Tablica gracza");
        for (int[] ints : playerBoard.board) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println(" ");
        }

        System.out.println("Tablica przeciwnika");
        for (int[] ints : enemyBoard.board) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println(" ");
        }
        enemyBoard.setSize(size);
        enemyBoard.showEnemyBoard();


        pickTime=true;

    }

    public boolean beginMarking(int x, int y){
        System.out.println("Jestem w beginMatrk");
        return enemyBoard.MarkShip(x,y);
    }


    public void setServerAdress(String serverAdress) {
        this.serverAdress = "127.0.0.1";
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

}
