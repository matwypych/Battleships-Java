package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class BattleShipServer {

    private static final int PORT = 9021;
    private static HashSet<String> names = new HashSet<String>();


    private static Vector<int[][]> PlayerBoards = new Vector<>();
    private static Vector<String> Picks = new Vector<>();
    public static int counter = 0;
    public static int counter1 = 0;
    public static int counter3 = 0;



    public static void main(String[] args) throws Exception {
      
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                Socket client = listener.accept();
                System.out.println("nowy klient");
                Handler h = new Handler(client);
                new Thread(h).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String request;
        private String request1;
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private ObjectOutputStream outputStreamPlayer;
        private ObjectInputStream inputStreamPlayer;
        private String tabInString = "";
        private String tabInString2 = "";
        private String answer = "";
        private StringBuilder answer1 = new StringBuilder();
        private String answer2 = "";


        public Handler(Socket socket) {
            this.socket = socket;
        }


        public void run() {

            boolean rdy = false;

            try {
                inputStreamPlayer = new ObjectInputStream(socket.getInputStream());
                outputStreamPlayer = new ObjectOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                    int[][] arrFromClient = (int[][]) inputStreamPlayer.readObject();
                    //outputStreamPlayer.writeObject(arrFromClient);

                    System.out.println("Serwer odebral tablice");
                    for (int[] ints : arrFromClient) {
                        for (int anInt : ints) {
                            System.out.print(anInt);
                        }
                        System.out.println(" ");
                    }

                    PlayerBoards.add(arrFromClient);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            System.out.println(tabInString);

            while(counter<2){
                try {
                    request=in.readLine();
                    //System.out.println(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(request.startsWith("Ready?")){
                    if(PlayerBoards.size()<2){
                        out.println("No");
                        out.flush();
                    }
                    else if(PlayerBoards.size()==2){

                            
                            for(int[][] tab : PlayerBoards){
                                for(int y=0;y<tab.length;y++){
                                    for(int x=0;x<tab.length;x++){
                                        if(tab[y][x]==1) {
                                            
                                            tabInString += y + "" + x + " ";
                                        }
                                    }
                                }
                            }
                            out.println(tabInString);
                            out.flush();

                        counter++;
                    }
                }
            }
            if (counter==2){
                boolean stop = false;
                boolean endgame = false;
                String mess = "";

                while(!stop){
                   
                    try {
                        String rec = in.readLine();
                        if(rec.startsWith("End1")){
                            
                            mess="1win";
                            Picks.add(mess);
                            endgame=true;
                        } else if(rec.startsWith("End2")){
                            
                            mess="2win";
                            Picks.add(mess);
                            endgame=true;
                        }
                        else
                        Picks.add(rec);
                       
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    boolean sent = false;

                    while(!sent){
                        if(Picks.size()==2 && endgame){
                            out.println(mess);
                            out.flush();
                            System.out.println(mess);
                            sent=true;
                        }
                        else if(Picks.size()==2){
                        
                            for(String tab : Picks){
                                answer1.append(tab);
                            }

                            out.println(answer1+" ");
                            out.flush();
                           
                            counter3++;
                            sent=true;

                            answer1 = new StringBuilder();
                        }
                        else{
                            out.println("Not");
                            out.flush();
                        }
                    }
                    if(counter3==2){
                       
                        if(Picks.size()>0 && Picks.size()<3){
                            Picks.clear();
                            answer1= new StringBuilder();
                        } else {
                            System.out.println("Sproboj jeszcze raz");
                        }

                       
                        counter3=0;
                        sent=false;
                    }

                }




            }

            }
        }
    }
