package sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class BattleShipClient extends Thread{

    static ObjectOutputStream outputStream;
    static ObjectInputStream inputStream;
    static DataInputStream inputStreamData;
    static DataOutputStream outputStreamData;
    static Socket socket;
    public static boolean ready=false;
    static Vector<int[][]> arrayFromServer;
    static BufferedReader in;
    static PrintWriter out;


    public BattleShipClient(int p, String h) throws IOException {
        socket =  new Socket(h, p);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void SendRequest(String s) {
        out.println(s);
        out.flush();
        //System.out.println("Wysylam " + s);
    }

    public String receiveRequest() throws IOException {
        String req = in.readLine();
        //System.out.println("Serwer wyslal: " + req);
        return req;
    }


    public static void SendBoardToServer(int[][] tab) throws Exception{
        outputStream.writeObject(tab);
        outputStream.flush();
    }


   public static Vector<int[][]> ReceiveBoardFromServer() throws Exception{

        inputStream = new ObjectInputStream(socket.getInputStream());
        try{
            arrayFromServer = (Vector<int[][]>) inputStream.readObject();
        } catch (EOFException | StreamCorruptedException | NullPointerException exception){
            exception.printStackTrace();
        }
        if(arrayFromServer!=null){

            System.out.println("w receive");
            for (int[][] tab : arrayFromServer) {
                for (int[] ints : tab) {
                    for (int anInt : ints) {
                        System.out.print(anInt);
                    }
                    System.out.println(" ");
                }
            }
            return arrayFromServer;
        }
        else return null;
    }


    public static void main(String[] args) throws Exception {
        //BattleShipClient client = new BattleShipClient();
    }
}