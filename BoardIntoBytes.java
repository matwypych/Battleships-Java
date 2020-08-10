package sample;

import java.io.*;

public class BoardIntoBytes implements Serializable {

    GameBoard g1;

    public BoardIntoBytes(GameBoard b1) {
        g1=b1;
    }

    public BoardIntoBytes convertBoard(GameBoard b1) throws IOException {

        return new BoardIntoBytes(b1);
    }

}
