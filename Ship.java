package sample;

import javafx.scene.Parent;

public class Ship extends Parent {
    public boolean vertical = true;

    private int health;

    public Ship(boolean vertical) {
        this.vertical = vertical;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}