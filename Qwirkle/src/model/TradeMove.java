package model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TradeMove extends Move {
    //TODO possible to use Tile object instead of number Set
    public final Set<Integer> tileNrs;
    public TradeMove (Set<Integer> t) {
        super(0, 0, null);
        this.tileNrs = t;
    }
}
