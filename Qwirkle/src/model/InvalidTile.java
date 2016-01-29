package model;

public class InvalidTile  extends Exception {

    public InvalidTile() {
        super("This tile cannot be removed");
    }
}
