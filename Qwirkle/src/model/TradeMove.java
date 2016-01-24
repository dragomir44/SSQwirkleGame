package model;


public class TradeMove extends Move {
    public final Tile tile;
    public TradeMove (Tile t) {
        super(0, 0, t);
        this.tile = t;
    }
}
