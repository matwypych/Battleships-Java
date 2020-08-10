package sample;

import javax.swing.*;

public class Controller {
    private int ships, size;
    private BattleShip battleShip1;
    boolean ctrl = false;
    JFrame frame = new JFrame("Battleships");

    public Controller(int ships, int size) {
        this.ships = ships;
        this.size = size;
    }

    public void setShips(int ships) {
        this.ships = ships;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public BattleShip check(){

        if(!ctrl){
            battleShip1 = new BattleShip(9021);
            battleShip1.ships = ships;
            battleShip1.size = size;
            System.out.println("Nie ma jeszcze planszy");
            ctrl = true;
        }
        return battleShip1;
    }
    public String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Chatter",
                JOptionPane.QUESTION_MESSAGE);
    }


    public String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }



}
