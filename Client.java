package sample;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class Client extends Thread{

    protected String name;
    protected Socket client;
    protected BufferedReader in;
    private ObjectOutputStream outputStreamPlayer;
    private ObjectInputStream inputStreamPlayer;
    private PrintWriter out;
    private static Vector<int[][]> PlayerBoards = new Vector<>();

    public Client(String name) {
        try {
            this.name = name;
            inputStreamPlayer = new ObjectInputStream(client.getInputStream());
            outputStreamPlayer = new ObjectOutputStream(client.getOutputStream());
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            int[][] arrFromClient = (int[][]) inputStreamPlayer.readObject();

            System.out.println("Serwer odebral tablice");
            for (int[] ints : arrFromClient) {
                for (int anInt : ints) {
                    System.out.print(anInt);
                }
                System.out.println(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}

