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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private static int hits = 0;


    public BattleShip(int p) {
        port = p;
    }



    public Parent createContent() throws Exception {



        BorderPane root = new BorderPane();
        root.setPrefSize(700, 900);

        if(ships == 2) {
            ships = 9;
            shipsToHit = ships;
            Text text3 = new Text("Masz do wyboru 2 statki" + '\n'
                    + "Pierwszy - 5 polowy" + '\n'
                    + "Drugi - 4 polowy");
            text3.setFont(Font.font("Arial",10));
            root.setRight(text3);
        }
        else if(ships == 3){
            ships = 12;
            shipsToHit = ships;
            Text text3 = new Text("Masz do wyboru 3 statki" + '\n'
                    + "Pierwszy - 5 polowy" + '\n'
                    + "Drugi - 4 polowy" + '\n'
                    + "Trzeci - 3 polowy");
            text3.setFont(Font.font("Arial",10));
            root.setRight(text3);

        }
        else if(ships == 5){
            ships = 14;
            shipsToHit = ships;
            Text text3 = new Text("Masz do wyboru 3 statki\" + '\\n'\n" +
                    "                    + \"Pierwszy - 5 polowy\" + '\\n'\n" +
                    "                    + \"Drugi - 4 polowy\" + '\\n'\n" +
                    "                    + \"Trzeci - 3 polowy\" + '\\n'\n" +
                    "                    + \"Czwarty - 2 polowy\" + '\\n'\n" +
                    "                    + \"Piąty - 1 polowy");
            text3.setFont(Font.font("Arial",10));
            root.setRight(text3);
        }


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Text text = new Text("Wybierz statki");
                Text text2 = new Text("Pól do wybrania: " + ships);

                text.setFont(Font.font("Arial Black",15));
                text2.setFont(Font.font("Arial Black",15));

                text.setFill(Color.BLACK);
                text2.setFill(Color.BLACK);

                root.setRight(text);
                root.setRight(text2);
            }
        });

        enemyBoard = new GameBoard(true, event -> {
            Thread t = new Thread(() -> {

                BattleShipClient.counter = 0;
                int hmt = 0;

            if (!endGame && pickTime) {

                Cell cell = (Cell) event.getSource();
                int counter1 = 0;
                int counter2 = 0;
                String ans = "";


                System.out.println(cell.x + " " + cell.y);
                System.out.println(endGame + "" + pickTime + "" + myTurn);
                System.out.println("Do trafienia" + shipsToHit);


        /////////////////////////// choosing


                enemyBoard.placeShip(new Ship(event.getButton() == MouseButton.PRIMARY), cell.x, cell.y);

                    if(beginMarking(cell.x, cell.y)){

                        hits++;
                        System.out.println("Trafienie ! Ilosc trafien: " + hits);
                        if(hits==shipsToHit){
                            endGame=true;
                            pickTime=false;
                            if(player1){
                                client.SendRequest("End1");
                                System.out.println("Koniec gry Gracz 1 wins");
                            }
                            else {
                                client.SendRequest("End2");
                                System.out.println("Koniec gry Gracz 2 wins");
                            }
                        }
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Text text = new Text("Poczekaj na ruch przeciwnika");
                            text.setFont(Font.font("Arial Black",25));
                            text.setFill(Color.RED);
                            root.setBottom(text);
                        }
                    });

                   if(!endGame)
                   {
                       //System.out.println("Wysylam picka : " + String.valueOf(cell.x) + String.valueOf(cell.y) );
                       client.SendRequest("tab" + String.valueOf(cell.x) + String.valueOf(cell.y));
                   }
                rec = false;


                while (!rec) {
                    try {
                        ans = client.receiveRequest();
                        System.out.println(ans);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ans.endsWith("Not")) {
                        System.out.println("jescze nie gotowe do odebrania");
                       // root.setBottom(new Text());
                    }
                    if (ans.endsWith(" ")) {
                        System.out.println("Jest gotowe");
                        rec = true;
                        System.out.println("Odberalem: " + ans);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Text text = new Text("Teraz twoj ruch");
                                text.setFont(Font.font("Arial Black",25));
                                text.setFill(Color.GREEN);
                                root.setBottom(text);
                            }
                        });

                        BattleShipClient.counter1++;
                        if(ans.endsWith("2win ")){
                            System.out.println("Wygrywa gracz 2");
                            rec=true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Text text = new Text("Wygrywa gracz 2");
                                    text.setFont(Font.font("Arial Black",25));
                                    text.setFill(Color.GREEN);
                                    root.setBottom(text);
                                }
                            });
                        }
                        else if(ans.endsWith("1win ")){
                            System.out.println("Wygrywa gracz 1");
                            rec=true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Text text = new Text("Wygrywa gracz 1");
                                    text.setFont(Font.font("Arial Black",25));
                                    text.setFill(Color.GREEN);
                                    root.setBottom(text);
                                }
                            });
                        }
                        else if(ans.startsWith("1win")){
                            System.out.println("Wygrywa gracz 1");
                            rec=true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Text text = new Text("Wygrywa gracz 1");
                                    text.setFont(Font.font("Arial Black",25));
                                    text.setFill(Color.GREEN);
                                    root.setBottom(text);
                                }
                            });
                        }
                        else if(ans.startsWith("2win")){
                            System.out.println("Wygrywa gracz 2");
                            rec=true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Text text = new Text("Wygrywa gracz 2");
                                    text.setFont(Font.font("Arial Black",25));
                                    text.setFill(Color.GREEN);
                                    root.setBottom(text);
                                }
                            });
                        }
                        else if(ans.startsWith("  ")){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Text text = new Text("Nie udalo sie wyslac twojego wyboru");
                                    text.setFont(Font.font("Arial Black",25));
                                    text.setFill(Color.RED);
                                    root.setBottom(text);
                                }
                            });
                        }
                        else{
                            picking(ans);
                        }

                    }
                    if(ans.endsWith("1win")){
                        System.out.println("Wygrywa gracz 1");
                        rec=true;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Text text = new Text("Wygrywa gracz 1");
                                text.setFont(Font.font("Arial Black",25));
                                text.setFill(Color.GREEN);
                                root.setBottom(text);
                            }
                        });
                    }
                    if(ans.endsWith("2win")){
                        System.out.println("Wygrywa gracz 2");
                        rec=true;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Text text = new Text("Wygrywa gracz 2");
                                text.setFont(Font.font("Arial Black",25));
                                text.setFill(Color.GREEN);
                                root.setBottom(text);
                            }
                        });
                    }
                }
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
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Text text2 = new Text("Pól do wybrania: " + ships);
                            text2.setFont(Font.font("Arial Black",15));
                            text2.setFill(Color.BLACK);
                            root.setRight(text2);
                        }
                    });
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
                            try {
                                beginGame();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Text text = new Text("Gracz 1");
                                    text.setFont(Font.font("Arial Black",20));
                                    text.setFill(Color.BLACK);

                                    Text text1 = new Text("Gracz 2");
                                    text1.setFont(Font.font("Arial Black",20));
                                    text1.setFill(Color.BLACK);


                                    if(player1){
                                        root.setTop(text);
                                    }else{
                                        root.setTop(text1);
                                    }

                                }
                            });

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

    private void picking(String data) {

        char[] tab = data.toCharArray();

        if(player1){
            if(size==10 || size == 15 || size==20){

                if(playerBoard.markHit(Integer.parseInt(String.valueOf(tab[8])),Integer.parseInt(String.valueOf(tab[9])))){
                    //hits++;
                    //System.out.println("Trafienia: " + hits);
                }

            }


        }
        else{
            if(size==10 || size == 15 || size==20){
                if(playerBoard.markHit(Integer.parseInt(String.valueOf(tab[3])),Integer.parseInt(String.valueOf(tab[4])))){
                    //hits++;
                    //System.out.println("Trafienia: " + hits);
                }

            }
        }

    }

    // funkcja wpisuje w tablice graczy zaznaczone wartosci przeslane
    // przez serwer

    private void beginGame() throws InterruptedException {

        char[] tab = Boards.toCharArray();
        System.out.println(Boards);
        System.out.println("rozmiar tab = " + tab.length);

        if (tab.length == 54) {
            for (int i = tab.length / 2; i < tab.length - 2; i += 3) {
                if (tab[i] != ' ') {
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i + 1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i]))][Integer.parseInt(String.valueOf(tab[i + 1]))] = 1;
                }
            }
            myTurn = true;
            player1 = true;
        } else if (tab.length == 58) {
            for (int i = 4; i < tab.length / 2; i += 3) {
                if (tab[i] != ' ') {
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i + 1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i]))][Integer.parseInt(String.valueOf(tab[i + 1]))] = 1;
                }
            }
            myTurn = true;
            player1 = false;
        } else if (tab.length == 72) {
            for (int i = tab.length / 2; i < tab.length - 2; i += 3) {
                if (tab[i] != ' ') {
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i + 1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i]))][Integer.parseInt(String.valueOf(tab[i + 1]))] = 1;
                }
            }
            myTurn = true;
            player1 = true;
        } else if (tab.length == 76) {
            for (int i = 4; i < tab.length / 2; i += 3) {
                if (tab[i] != ' ') {
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i + 1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i]))][Integer.parseInt(String.valueOf(tab[i + 1]))] = 1;
                }
            }
            myTurn = true;
            player1 = false;
        }
        else if (tab.length == 84) {
            for (int i = tab.length / 2; i < tab.length - 2; i += 3) {
                if (tab[i] != ' ') {
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i + 1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i]))][Integer.parseInt(String.valueOf(tab[i + 1]))] = 1;
                }
            }
            myTurn = true;
            player1 = true;
        }
        else if (tab.length == 88) {
            for (int i = 4; i < tab.length / 2; i += 3) {
                if (tab[i] != ' ') {
                    System.out.println(String.valueOf(tab[i]) + " . " + String.valueOf(tab[i + 1]));
                    enemyBoard.board[Integer.parseInt(String.valueOf(tab[i]))][Integer.parseInt(String.valueOf(tab[i + 1]))] = 1;
                }
            }
            myTurn = true;
            player1 = false;
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


        pickTime = true;

    }

    private boolean beginMarking(int x, int y){
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
