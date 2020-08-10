package sample;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LastInt extends Application {

    GameBoard playerBoard;
    GameBoard enemyBoard;
    public int size;
    public int ships;

    public LastInt(int size, int ships) {
        this.size = size;
        this.ships = ships;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        stage.setTitle("Battleship");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public Parent createContent(){
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);


        enemyBoard = new GameBoard(false, event -> {
            Cell cell = (Cell) event.getSource();
            if (enemyBoard.placeShip(new Ship(event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                enemyBoard.board[cell.x][cell.y]=0;
            }
        },size);


        return root;
    }
}
