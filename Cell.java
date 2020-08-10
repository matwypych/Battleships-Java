package sample;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    public int x, y;
    public boolean shooted = false;
    private VBox rows = new VBox();
    private GameBoard board;
    public Ship ship = null;
    public boolean wasShot = false;

    public Cell(int x, int y, GameBoard board) {
        super(30, 30);
        this.x = x;
        this.y = y;
        this.board = board;
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

}
