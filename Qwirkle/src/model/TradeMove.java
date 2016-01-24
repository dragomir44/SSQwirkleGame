package model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TradeMove extends Move {
    public final Set<Integer> tileNrs;
    public TradeMove (ArrayList<Integer> t) {
        super(0, 0, null);
        this.tileNrs = new HashSet<Integer>(t);
    }
}
