package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application implements EventHandler<ActionEvent> {

    Button ButtonId;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("BattleShips");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent actionEvent) {

        Stage newStage = new Stage();

        Parent middleInt = null;
        try {
            middleInt = FXMLLoader.load(getClass().getResource("MiddleInt.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newStage.setScene(new Scene(middleInt,600,400));
        newStage.setTitle("Battleships");
        newStage.show();


    }


}
