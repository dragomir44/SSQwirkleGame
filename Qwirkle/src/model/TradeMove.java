package model;

import java.util.Set;

public class TradeMove extends Move {
    public final Set<Integer> tileNrs;
    public TradeMove(Set<Integer> t) {
        super(0, 0, null);
        this.tileNrs = t;
    }
}
