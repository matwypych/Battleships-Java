package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.Serializable;

public class GameBoard extends Parent implements EventHandler<ActionEvent>, Serializable {

    private int size,ships;
    Button selectShipsId;
    BorderPane mainBorderPane;
    GridPane mainGridPane;
    private VBox rows = new VBox();
    private boolean enemy = false;
    int[][] board;

    public GameBoard(boolean enemy, EventHandler<? super MouseEvent> handler, int size){
        this.enemy = enemy;
        board = new int[size][size];
        for (int y = 0; y < size; y++) {
            HBox row = new HBox();
            for (int x = 0; x < size; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
                board[x][y]=0;
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }



    public void showEnemyBoard(){

        for(int y = 0; y<size;y++) {
            for(int x=0;x<size;x++) {
                if(board[x][y]==1){
                    Cell cell = (Cell)((HBox)rows.getChildren().get(x)).getChildren().get(y);
                    cell.setFill(javafx.scene.paint.Color.BLACK);
                    cell.setStroke(javafx.scene.paint.Color.GREEN);
                }
            }
        }
    }

    public boolean placeShip(Ship ship, int x, int y) {

        Cell cell = (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
        cell.ship = ship;
        if (!enemy) {
            cell.setFill(javafx.scene.paint.Color.WHITE);
            cell.setStroke(javafx.scene.paint.Color.GREEN);
        }
            return true;
    }

    public boolean MarkShip(int x, int y){
        Cell cell = (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
        if (board[y][x]==1) {
            cell.setFill(javafx.scene.paint.Color.RED);
            cell.setStroke(javafx.scene.paint.Color.BLACK);
            return true;
        }
        else {
            cell.setFill(javafx.scene.paint.Color.BLACK);
            cell.setStroke(javafx.scene.paint.Color.BLACK);
            return false;
        }
    }

    public boolean markHit(int x, int y){
        Cell cell = (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
        if (board[y][x]==1) {
            cell.setFill(javafx.scene.paint.Color.RED);
            cell.setStroke(javafx.scene.paint.Color.BLACK);
            return true;
        }
        else {
            cell.setFill(javafx.scene.paint.Color.BLACK);
            cell.setStroke(javafx.scene.paint.Color.BLACK);
            return false;
        }
    }


    @Override
    public void handle(ActionEvent actionEvent) {

    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setShips(int ships) {
        this.ships = ships;
    }


    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

}
