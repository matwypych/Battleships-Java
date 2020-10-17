package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.BLACK;

public class MiddleInt extends Application implements EventHandler<ActionEvent> {

    Button size10,size15,size20,ships3,ships5,ships7,play;
   static public int size=10;
   static public int ships=2;
   static Controller controller=new Controller(0,0);


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception  {
        Parent middleInt = FXMLLoader.load(getClass().getResource("MiddleInterface.fxml"));
        primaryStage.setTitle("BattleShips");
        primaryStage.setScene(new Scene(middleInt, 600, 400));
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }

    public void setSize10(ActionEvent actionEvent){ size = 10;}
    public void setSize15(ActionEvent actionEvent){
        size = 15;
    }
    public void setSize20(ActionEvent actionEvent){
        size = 20;
    }
    public void setShips3(ActionEvent actionEvent){
        ships=2;
    }
    public void setShips5(ActionEvent actionEvent){
        ships=3;
    }
    public void setShips7(ActionEvent actionEvent){
        ships=5;
    }

    public void play(ActionEvent actionEvent) throws Exception {

        Stage newStage = new Stage();
        controller.setShips(ships);
        controller.setSize(size);
        String playerName = controller.getName();
        String serverAddress = controller.getServerAddress();
        BattleShip battleShip = controller.check();
        battleShip.setPlayerName(playerName);
        battleShip.setServerAdress(serverAddress);
        battleShip.start(newStage);
    }
}
