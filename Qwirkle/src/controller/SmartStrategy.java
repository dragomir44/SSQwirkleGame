package controller;

import java.util.*;

import model.*;

public class SmartStrategy implements Strategy {
    private String name = "Smart";

    public String getName() {
        return name;
    }

    public ArrayList<Move> determineMove(Board board, Hand hand) {
        ArrayList<Move> moves = new ArrayList<Move>();
        TreeMap<ArrayList<Move>, Integer> possibleMoves;
        System.out.println(hand);
        possibleMoves = board.getPossibleMoves(hand.getTiles());
        // smart Strategy:
        //  don't create rows of 5

        if (!possibleMoves.isEmpty()) {
            moveLoop:
            for (Map.Entry<ArrayList<Move>, Integer> entry : possibleMoves.entrySet()) {
                ArrayList<Move> mMoves = entry.getKey();
                int score = entry.getValue();
                ArrayList<ArrayList<Tile>> newLines = board.getAllLines(mMoves);
                for (ArrayList<Tile> newLine : newLines) {
                    if (hand.getTiles().size() == 6) {
                        if (newLine.size() != 5 && (score > 3 || board.isEmpty())) { // don't create lines of 5
                            moves = mMoves;
                            break moveLoop;
                        }
                    } else if (moves.size() == 2 || score > 4) { // endgame situation
                        System.err.println("Endgame Situation");
                        moves = mMoves;
                        break moveLoop;
                    }
                }
            }
            if (moves.isEmpty()) { // make sure no empty move is returned
                moves = possibleMoves.firstKey();
            }
        } else {
            ArrayList<Integer> tileNrs = new ArrayList<Integer>();
            tileNrs.add(1);
            moves.add(new TradeMove(tileNrs));
        }
        return moves;
    }
}
